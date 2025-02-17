package com.practicum.playlistmaker.player.ui.model

sealed interface PlayerViewState{
    object Prepare : PlayerViewState
    object Play : PlayerViewState
    object Pause : PlayerViewState
}