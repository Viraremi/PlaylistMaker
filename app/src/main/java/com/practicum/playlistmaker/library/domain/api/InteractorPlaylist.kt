package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface InteractorPlaylist {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    suspend fun getTracksFromPlaylist(playlist: Playlist): Flow<List<Track>>

    suspend fun getPlaylistById(playlistId: Int): Playlist
}