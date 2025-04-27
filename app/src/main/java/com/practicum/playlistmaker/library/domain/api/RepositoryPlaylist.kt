package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface RepositoryPlaylist {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>
}