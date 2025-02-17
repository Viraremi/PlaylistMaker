package com.practicum.playlistmaker.search.ui.model

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface SearchState {
    object Loading : SearchState
    data class ConnectionError(val msg: String) : SearchState
    data class EmptyError(val msg: String) : SearchState
    data class Content(val data: List<Track>) : SearchState
}