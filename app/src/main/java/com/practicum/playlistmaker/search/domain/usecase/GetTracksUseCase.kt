package com.practicum.playlistmaker.search.domain.usecase

import com.practicum.playlistmaker.search.domain.api.RepositoryNetwork
import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTracksUseCase(
    private val repository: RepositoryNetwork
) {
    fun execute(searchText: String): Flow<Pair<List<Track>?, String?>> {
        return repository.getTracks(searchText).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.msg)
                }
            }
        }
    }
}