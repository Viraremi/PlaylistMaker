package com.practicum.playlistmaker.library.domain.interactror

import com.practicum.playlistmaker.library.domain.api.InteractorFavorite
import com.practicum.playlistmaker.library.domain.api.RepositoryFavorite
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class InteractorFavoriteImpl(
    private val repository: RepositoryFavorite
) : InteractorFavorite {

    override suspend fun addToFavorite(track: Track) {
        repository.addToFavorite(track)
    }

    override suspend fun deleteFromFavorite(track: Track) {
        repository.deleteFromFavorite(track)
    }

    override suspend fun getFavorite(): Flow<List<Track>> {
        return repository.getFavorite()
    }
}