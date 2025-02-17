package com.practicum.playlistmaker.player.ui.model

sealed interface PlayerState{
    object Play : PlayerState
    object Pause : PlayerState
}