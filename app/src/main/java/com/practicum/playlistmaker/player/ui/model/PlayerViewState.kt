package com.practicum.playlistmaker.player.ui.model

sealed interface PlayerViewState{
    object Prepare : PlayerViewState
    data class Play(val data: String) : PlayerViewState
    object Pause : PlayerViewState
}