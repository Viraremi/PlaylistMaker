package com.practicum.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlayerViewState
import com.practicum.playlistmaker.util.Creator

class PlayerViewModel() : ViewModel() {

    companion object {
        private const val DELAY = 1000L
    }

    val interactor = Creator.providePlayerInteractor()

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
        statePlayer.value = PlayerViewState.Play
    }

    fun pause(){
        interactor.pause()
        statePlayer.value = PlayerViewState.Pause
    }

    fun release(){
        interactor.release()
    }
}