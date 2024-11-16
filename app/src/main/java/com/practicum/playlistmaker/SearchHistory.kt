package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(
    private val sharedPref: SharedPreferences,
    private val gson: Gson
) {
    private var history = mutableListOf<Track>()
    fun getHistory(): MutableList<Track> { return history }

    companion object{
        private const val TRACKS_KEY = "tracks"
        private const val MAX_SIZE = 10
    }

    init{
        val json = sharedPref.getString(TRACKS_KEY, null)
        history = gson.fromJson(json, object : TypeToken<MutableList<Track>>() {}.type) ?: mutableListOf<Track>()
    }

    private fun save(){
        val json = gson.toJson(history)
        sharedPref.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    fun clear(){
        history.clear()
        sharedPref.edit()
            .clear()
            .apply()
        save()
    }

    fun add(track: Track){
        if (history.contains(track)){
            history.remove(track)
        }
        if (history.size >= MAX_SIZE){
            history.removeLast()
        }
        history.add(0, track)
        save()
    }
}