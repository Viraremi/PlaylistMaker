package com.practicum.playlistmaker.library.data.db.convertors

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.domain.model.Playlist

class PlaylistConvertor(
    private val gson: Gson
) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.imgPath,
            gson.toJson(playlist.tracksList),
            playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.imgPath,
            gson.fromJson(playlist.tracksList, object : TypeToken<List<Int>>() {}.type),
            playlist.tracksCount
        )
    }

    fun fromTrackListToJson(list: List<Int>): String{
        return gson.toJson(list)
    }
}