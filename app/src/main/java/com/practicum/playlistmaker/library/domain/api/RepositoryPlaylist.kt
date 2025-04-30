package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface RepositoryPlaylist {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updateTracklist(playlistId: Int, trackIdsList: List<Int>)

    suspend fun getSavedTracks(trackIDs: List<Int>): Flow<List<Track>>

    suspend fun addSavedTrack(track: Track)

    suspend fun getPlaylistById(playlistId: Int): Playlist
}