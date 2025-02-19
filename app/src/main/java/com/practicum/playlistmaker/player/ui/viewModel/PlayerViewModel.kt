package com.practicum.playlistmaker.player.ui.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.InteractorPlayer
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlayerViewState
import com.practicum.playlistmaker.search.domain.api.InteractorHistory
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.TimeFormatter

class PlayerViewModel(
    val interactorPlayer: InteractorPlayer,
    val interactorHistory: InteractorHistory
) : ViewModel() {

    companion object {
        private const val DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    private val statePlayerView = MutableLiveData<PlayerViewState>()
    fun getStatePlayerView(): LiveData<PlayerViewState> = statePlayerView

    fun prepare(url: String){
        interactorPlayer.prepare(
            url = url,
            onComplete = {
                statePlayerView.value = PlayerViewState.Prepare
                handler.removeCallbacksAndMessages(null)
            }
        )
    }

    fun playbackControl(){
        when(interactorPlayer.getPlayerState()) {
            PlayerState.PLAYING -> { pause() }
            PlayerState.PREPARED, PlayerState.PAUSED -> { play() }
            else -> return
        }
    }

    fun play(){
        handler.post(createUpdateTimerTask())
        interactorPlayer.play()
        statePlayerView.value = PlayerViewState.Play(
            TimeFormatter.getValidTimeFormat(interactorPlayer.getPosition().toLong())
        )
    }

    fun pause(){
        interactorPlayer.pause()
        statePlayerView.value = PlayerViewState.Pause
        handler.removeCallbacksAndMessages(null)
    }

    fun release(){
        interactorPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (interactorPlayer.getPlayerState() == PlayerState.PLAYING) {
                    val elapsedTime = interactorPlayer.getPosition()
                    statePlayerView.value = PlayerViewState.Play(
                        TimeFormatter.getValidTimeFormat(elapsedTime.toLong())
                    )
                    handler.postDelayed(this, DELAY)
                }
            }
        }
    }

    fun getTrackById(index: Int): Track{
        return interactorHistory.getHistory()[index]
    }
}