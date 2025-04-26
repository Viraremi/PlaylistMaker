package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.library.data.db.AppDB
import com.practicum.playlistmaker.search.domain.api.RepositoryHistory
import com.practicum.playlistmaker.search.domain.model.Track

class RepositoryHistoryImpl(
    private val sharedPref: SharedPreferences,
    private val gson: Gson,
    private val db: AppDB
) : RepositoryHistory {

    companion object{
        private const val TRACKS_KEY = "tracks"
        private const val MAX_SIZE = 10
    }

    private var history = mutableListOf<Track>()

    init{
        val json = sharedPref.getString(TRACKS_KEY, null)
        history = gson.fromJson(json, object : TypeToken<MutableList<Track>>() {}.type) ?: mutableListOf<Track>()
        val favoritesIDs = db.favoriteDao().getIDsFavorite()
        history.map { track ->
            track.isFavorite = track.trackId in favoritesIDs
        }
    }

    override fun getHistory(): MutableList<Track> {
        return history
    }

    override fun clear() {
        history.clear()
        sharedPref.edit()
            .clear()
            .apply()
        save()
    }

    override fun getIdByTrack(track: Track): Int? {
        val id = history.indexOf(track)
        return if (id == -1) { null } else { id }
    }

    override fun add(item: Track) {
        if (history.contains(item)){
            history.remove(item)
        }
        if (history.size >= MAX_SIZE){
            history.removeLast()
        }
        history.add(0, item)
        save()
    }

    private fun save(){
        val json = gson.toJson(history)
        sharedPref.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }
}