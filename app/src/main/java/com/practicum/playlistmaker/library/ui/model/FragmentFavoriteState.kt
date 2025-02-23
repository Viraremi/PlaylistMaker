package com.practicum.playlistmaker.library.ui.model

sealed interface FragmentFavoriteState {
    data object EMPTY: FragmentFavoriteState
}