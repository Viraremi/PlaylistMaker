package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface RepositoryHistory {
    fun getHistory(): MutableList<Track>
    fun add(item: Track)
    fun clear()
    fun getIdByTrack(track: Track): Int?
}