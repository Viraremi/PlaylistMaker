package com.practicum.playlistmaker.player.domain.interactor

import com.practicum.playlistmaker.player.domain.api.InteractorPlayer
import com.practicum.playlistmaker.player.domain.api.RepositoryPlayer
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.util.StringFormatter

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

    override fun getPosition(): String {
        return StringFormatter.getValidTimeFormat(repository.getPosition().toLong())
    }

    override fun getPlayerState(): PlayerState {
        return repository.getPlayerState()
    }
}