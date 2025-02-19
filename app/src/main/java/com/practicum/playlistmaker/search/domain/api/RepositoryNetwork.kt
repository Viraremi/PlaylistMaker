package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track

interface RepositoryNetwork {
    fun getTracks(currency: String): Resource<List<Track>>
}