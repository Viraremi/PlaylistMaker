package com.practicum.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.InteractorFavorite
import com.practicum.playlistmaker.player.domain.api.InteractorPlayer
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlayerViewState
import com.practicum.playlistmaker.search.domain.api.InteractorHistory
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val interactorPlayer: InteractorPlayer,
    private val interactorHistory: InteractorHistory,
    private val interactorFavorite: InteractorFavorite
) : ViewModel() {

    companion object {
        private const val DELAY = 1000L
        private const val TOKEN_TIMER = "TIMER"
    }

    private val stateFavorite = MutableLiveData<Boolean>()
    fun getStateFavorite(): LiveData<Boolean> = stateFavorite

    fun onClickFavorite(track: Track){
        viewModelScope.launch {
            if (track.isFavorite) {
                interactorFavorite.deleteFromFavorite(track)
                stateFavorite.postValue(!track.isFavorite)
            }
            else {
                interactorFavorite.addToFavorite(track)
                stateFavorite.postValue(!track.isFavorite)
            }
        }
    }

    private var timerJob: Job? = null

    private val statePlayerView = MutableLiveData<PlayerViewState>()
    fun getStatePlayerView(): LiveData<PlayerViewState> = statePlayerView

    fun prepare(url: String){
        interactorPlayer.prepare(
            url = url,
            onComplete = {
                statePlayerView.value = PlayerViewState.Prepared()
                timerJob?.cancel()
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

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (interactorPlayer.getPlayerState() == PlayerState.PLAYING) {
                delay(300L)
                statePlayerView.postValue(PlayerViewState.Playing(interactorPlayer.getPosition()))
            }
        }
    }

    fun play(){
        interactorPlayer.play()
        startTimer()
    }

    fun pause(){
        interactorPlayer.pause()
        timerJob?.cancel()
        statePlayerView.value = PlayerViewState.Paused(interactorPlayer.getPosition())
    }

    fun release(){
        interactorPlayer.release()
        statePlayerView.value = PlayerViewState.Default()
    }

    fun getTrackById(index: Int): Track{
        return interactorHistory.getHistory()[index]
    }
}