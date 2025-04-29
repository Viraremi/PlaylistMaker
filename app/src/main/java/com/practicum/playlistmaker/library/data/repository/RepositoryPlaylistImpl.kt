package com.practicum.playlistmaker.library.data.repository

import com.practicum.playlistmaker.library.data.db.AppDB
import com.practicum.playlistmaker.library.data.db.convertors.PlaylistConvertor
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.domain.api.RepositoryPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RepositoryPlaylistImpl(
    private val db: AppDB,
    private val convertor: PlaylistConvertor
) : RepositoryPlaylist {

    override suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            db.playlistDao().insertPlaylist(convertor.map(playlist))
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            db.playlistDao().deletePlaylists(convertor.map(playlist))
        }
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = db.playlistDao().getPlaylists()
        emit(convertFromEntity(playlists))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateTracklist(playlistId: Int, trackIdsList: List<Int>) {
        withContext(Dispatchers.IO) {
            db.playlistDao().updateTrackList(playlistId, convertor.fromTrackListToJson(trackIdsList))
        }
    }

    private fun convertFromEntity(playlists: List<PlaylistEntity>): List<Playlist>{
        return playlists.map { playlist -> convertor.map(playlist) }
    }
}