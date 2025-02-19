package com.practicum.playlistmaker.main.ui.model

import com.practicum.playlistmaker.main.ui.viewModel.MainViewModel

sealed interface MainViewState {
    object SEARCH : MainViewState
    object LIBRARY : MainViewState
    object SETTINGS : MainViewState
}