package com.practicum.playlistmaker.player.domain.interactor

import com.practicum.playlistmaker.player.domain.api.InteractorPlayer
import com.practicum.playlistmaker.player.domain.api.RepositoryPlayer
import com.practicum.playlistmaker.player.domain.model.PlayerState

class InteractorPlayerImpl(
    private val repository: RepositoryPlayer
): InteractorPlayer {
    override fun prepare(url: String, onComplete: () -> Unit) {
        repository.prepare(url, onComplete)
    }

    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun release() {
        repository.release()
    }

    override fun playbackControl() {
        repository.playbackControl()
    }

    override fun getPosition(): Int {
        return repository.getPosition()
    }

    override fun getPlayerState(): PlayerState {
        return repository.getPlayerState()
    }
}