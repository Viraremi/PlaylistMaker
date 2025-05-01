package com.practicum.playlistmaker.library.data.repository

import com.practicum.playlistmaker.library.data.db.AppDB
import com.practicum.playlistmaker.library.data.db.convertors.PlaylistConvertor
import com.practicum.playlistmaker.library.data.db.convertors.SavedTracksConvertor
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.SavedTracksEntity
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
    private val convertorPlaylist: PlaylistConvertor,
    private val convertorSavedTracks: SavedTracksConvertor
) : RepositoryPlaylist {

    override suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            db.playlistDao().insertPlaylist(convertorPlaylist.map(playlist))
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            db.playlistDao().deletePlaylists(convertorPlaylist.map(playlist))
        }
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = db.playlistDao().getPlaylists()
        emit(convertPlaylistFromEntity(playlists))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateTracklist(playlistId: Int, trackIdsList: List<Int>) {
        withContext(Dispatchers.IO) {
            db.playlistDao().updateTrackList(
                playlistId,
                convertorPlaylist.fromTrackListToJson(trackIdsList),
                trackIdsList.size
            )
        }
    }

    override suspend fun getSavedTracks(trackIDs: List<Int>): Flow<List<Track>> = flow {
        val savedTracks = db.savedTracksDao().getTrackList(trackIDs)
        emit(convertSavedTracksFromEntity(savedTracks))
    }.flowOn(Dispatchers.IO)

    override suspend fun addSavedTrack(track: Track) {
        withContext(Dispatchers.IO) {
            db.savedTracksDao().insertSavedTrack(convertorSavedTracks.map(track))
        }
    }

    override suspend fun deleteSavedTrack(track: Track) {
        withContext(Dispatchers.IO) {
            getPlaylists().collect { playlists ->
                for (item in playlists) {
                    if (track.trackId in item.tracksList) {
                        return@collect
                    }
                }
                db.savedTracksDao().deleteSavedTrack(convertorSavedTracks.map(track))
            }
        }
    }

    private fun convertPlaylistFromEntity(playlists: List<PlaylistEntity>): List<Playlist>{
        return playlists.map { playlist -> convertorPlaylist.map(playlist) }
    }

    private fun convertSavedTracksFromEntity(tracks: List<SavedTracksEntity>): List<Track>{
        return tracks.map { track -> convertorSavedTracks.map(track) }
    }
}