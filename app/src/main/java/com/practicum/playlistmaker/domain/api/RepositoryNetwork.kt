package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.Resource
import com.practicum.playlistmaker.domain.model.Track

interface RepositoryNetwork {
    fun getTracks(currency: String): Resource<List<Track>>
}