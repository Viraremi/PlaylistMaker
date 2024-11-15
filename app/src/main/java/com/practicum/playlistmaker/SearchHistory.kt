package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(
    private val sharedPref: SharedPreferences
) {
    var history = mutableListOf<Track>()
    companion object{
        const val TRACKS_KEY = "tracks"
        const val MAX_SIZE = 8 //если установить значение больше, то кнопка очистки истории уйдет за экран... я не знаю как это поправить ._.
    }

    init{
        val json = sharedPref.getString(TRACKS_KEY, null)
        history = Gson().fromJson(json, object : TypeToken<MutableList<Track>>() {}.type) ?: mutableListOf<Track>()
    }

    fun save(){
        val json = Gson().toJson(history)
        sharedPref.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    fun clear(){
        history.clear()
        sharedPref.edit()
            .clear()
            .apply()
    }

    fun add(track: Track){
        if (history.contains(track)){
            history.remove(track)
        }
        if (history.size >= MAX_SIZE){
            history.removeLast()
        }
        history.add(0, track)
    }
}