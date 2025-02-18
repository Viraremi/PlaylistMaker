package com.practicum.playlistmaker.player.ui.viewModel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlayerViewState
import com.practicum.playlistmaker.util.Creator
import java.util.Locale

class PlayerViewModel() : ViewModel() {

    val interactor = Creator.providePlayerInteractor()

    private val statePlayerView = MutableLiveData<PlayerViewState>()
    fun getStatePlayerView(): LiveData<PlayerViewState> = statePlayerView

    fun prepare(url: String){
        interactor.prepare(url = url)
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
        statePlayerView.value = PlayerViewState.Play
    }

    fun pause(){
        interactor.pause()
        statePlayerView.value = PlayerViewState.Pause
    }

    fun release(){
        interactor.release()
    }

    fun getPlayerState(): PlayerState{
        return interactor.getPlayerState()
    }

    fun getPlayerPosition(): Int{
        return interactor.getPosition()
    }

    val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
}