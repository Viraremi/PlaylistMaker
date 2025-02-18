package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.PlayerState

interface InteractorPlayer {
    fun prepare(url: String, onComplete: () -> Unit)
    fun play()
    fun pause()
    fun release()
    fun playbackControl()
    fun getPosition(): Int
    fun getPlayerState(): PlayerState
}