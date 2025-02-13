package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.Track

interface RepositoryHistory {
    fun getHistory(): MutableList<Track>
    fun add(item: Track)
    fun clear()
}