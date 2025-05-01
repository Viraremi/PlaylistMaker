package com.practicum.playlistmaker.library.domain.interactror

import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.api.RepositoryPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class InteractorPlaylistImpl(
    private val repository: RepositoryPlaylist
) : InteractorPlaylist {

    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        repository.addSavedTrack(track)
        repository.updateTracklist(playlist.id, playlist.tracksList + track.trackId)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {
        repository.deleteSavedTrack(track)
        repository.updateTracklist(playlist.id, playlist.tracksList - track.trackId)
    }

    override suspend fun getTracksFromPlaylist(playlist: Playlist): Flow<List<Track>> {
        return repository.getSavedTracks(playlist.tracksList)
    }
}