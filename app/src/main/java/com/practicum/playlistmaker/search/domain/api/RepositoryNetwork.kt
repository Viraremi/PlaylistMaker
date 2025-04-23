package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface RepositoryNetwork {
    fun getTracks(currency: String): Flow<Resource<List<Track>>>
}