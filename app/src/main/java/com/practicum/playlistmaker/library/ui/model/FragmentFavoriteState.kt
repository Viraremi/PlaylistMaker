package com.practicum.playlistmaker.library.ui.model

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface FragmentFavoriteState {
    data object Empty: FragmentFavoriteState
    data class Content(val data: List<Track>): FragmentFavoriteState
}