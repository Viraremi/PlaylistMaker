package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface InteractorFavorite {

    suspend fun addToFavorite(track: Track)

    suspend fun deleteFromFavorite(track: Track)

    suspend fun getFavorite(): Flow<List<Track>>
}