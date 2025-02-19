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

class PlayerViewModel(
    val interactorPlayer: InteractorPlayer,
    val interactorHistory: InteractorHistory
) : ViewModel() {

    companion object {
        private const val DELAY = 1000L
        private const val TOKEN_TIMER = "TIMER"
    }

    private val handler = Handler(Looper.getMainLooper())

    private val statePlayerView = MutableLiveData<PlayerViewState>()
    fun getStatePlayerView(): LiveData<PlayerViewState> = statePlayerView

    fun prepare(url: String){
        interactorPlayer.prepare(
            url = url,
            onComplete = {
                statePlayerView.value = PlayerViewState.Prepare
                handler.removeCallbacksAndMessages(TOKEN_TIMER)
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
        interactorPlayer.play()
        handler.removeCallbacksAndMessages(TOKEN_TIMER)
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    statePlayerView.value = PlayerViewState.Play(interactorPlayer.getPosition())
                    handler.postDelayed(this, TOKEN_TIMER, DELAY)
                }
            },
            TOKEN_TIMER,
            DELAY
        )
    }

    fun pause(){
        interactorPlayer.pause()
        statePlayerView.value = PlayerViewState.Pause
        handler.removeCallbacksAndMessages(TOKEN_TIMER)
    }

    fun release(){
        interactorPlayer.release()
        handler.removeCallbacksAndMessages(TOKEN_TIMER)
    }

    fun getTrackById(index: Int): Track{
        return interactorHistory.getHistory()[index]
    }
}