package com.practicum.playlistmaker.search.ui.model

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface SearchHistoryState {
    data class HasValue(val data: List<Track>) : SearchHistoryState
    object Empty : SearchHistoryState
}