package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface InteractorHistory {
    fun getHistory(): MutableList<Track>
    fun add(item: Track)
    fun clear()
}