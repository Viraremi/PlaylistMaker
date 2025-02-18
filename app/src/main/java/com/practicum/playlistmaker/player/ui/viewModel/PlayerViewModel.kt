package com.practicum.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlayerViewState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.Creator

class PlayerViewModel() : ViewModel() {

    val interactorPlayer = Creator.providePlayerInteractor()
    val interactorHistory = Creator.provideInteractorHistory()

    private val statePlayerView = MutableLiveData<PlayerViewState>()
    fun getStatePlayerView(): LiveData<PlayerViewState> = statePlayerView

    fun prepare(url: String){
        interactorPlayer.prepare(url = url)
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
        statePlayerView.value = PlayerViewState.Play
    }

    fun pause(){
        interactorPlayer.pause()
        statePlayerView.value = PlayerViewState.Pause
    }

    fun release(){
        interactorPlayer.release()
    }

    fun getPlayerState(): PlayerState{
        return interactorPlayer.getPlayerState()
    }

    fun getPlayerPosition(): Int{
        return interactorPlayer.getPosition()
    }

    fun getTrackById(index: Int): Track{
        return interactorHistory.getHistory()[index]
    }
}