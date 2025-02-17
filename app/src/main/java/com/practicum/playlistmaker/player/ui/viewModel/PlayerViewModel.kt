package com.practicum.playlistmaker.player.ui.viewModel

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlayerViewState
import com.practicum.playlistmaker.util.Creator
import java.util.Locale

class PlayerViewModel() : ViewModel() {

    companion object {
        private const val DELAY = 1000L
    }

    val interactor = Creator.providePlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())

    private val statePlayer = MutableLiveData<PlayerViewState>()
    fun getStatePlayer(): LiveData<PlayerViewState> = statePlayer

    private val stateTimer = MutableLiveData<String>()
    fun getStateTimer(): LiveData<String> = stateTimer

    fun prepare(url: String){
        interactor.prepare(
            url = url,
            onComplete = {
                statePlayer.value = PlayerViewState.Prepare
            }
        )
    }

    fun playbackControl(){
        when(interactor.getPlayerState()) {
            PlayerState.PLAYING -> { pause() }
            PlayerState.PREPARED, PlayerState.PAUSED -> { play() }
            else -> return
        }
    }

    fun play(){
        interactor.play()
        handler.post(createUpdateTimerTask())
        statePlayer.value = PlayerViewState.Play
    }

    fun pause(){
        interactor.pause()
        statePlayer.value = PlayerViewState.Pause
        handler.removeCallbacksAndMessages(null)
    }

    fun release(){
        interactor.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (interactor.getPlayerState() == PlayerState.PLAYING) {
                    val elapsedTime = interactor.getPosition()
                    stateTimer.value = dateFormat.format(elapsedTime.toLong())
                    handler.postDelayed(this, DELAY)
                }
            }
        }
    }
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
}