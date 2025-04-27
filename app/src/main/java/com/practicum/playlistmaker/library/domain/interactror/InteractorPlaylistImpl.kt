package com.practicum.playlistmaker.library.domain.interactror

import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.api.RepositoryPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
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
}