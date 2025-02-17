package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.PlayerState

interface RepositoryPlayer {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun playbackControl()
    fun getPosition(): Int
    fun getPlayerState(): PlayerState
}