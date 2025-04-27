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
            name = playlist.name,
            description = playlist.description,
            imgPath = playlist.imgPath,
            tracksList = gson.toJson(playlist.tracksList),
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.name,
            playlist.description,
            playlist.imgPath,
            gson.fromJson(playlist.tracksList, object : TypeToken<List<Int>>() {}.type),
            playlist.tracksCount
        )
    }
}