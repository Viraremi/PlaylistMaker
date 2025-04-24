package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.RepositoryPlayer
import com.practicum.playlistmaker.player.domain.model.PlayerState

class RepositoryPlayerImpl(
    private var mediaPlayer: MediaPlayer
): RepositoryPlayer {

    private var playerState = PlayerState.DEFAULT
    override fun getPlayerState(): PlayerState{ return playerState }

    override fun playbackControl() {
        when(playerState) {
            PlayerState.PLAYING -> { pause() }
            PlayerState.PREPARED, PlayerState.PAUSED -> { play() }
            else -> return
        }
    }

    override fun getPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun prepare(url: String, onComplete: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { onComplete.invoke() }
        mediaPlayer.setOnCompletionListener { onComplete.invoke() }
        playerState = PlayerState.PREPARED
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
        playerState = PlayerState.DEFAULT
    }
}