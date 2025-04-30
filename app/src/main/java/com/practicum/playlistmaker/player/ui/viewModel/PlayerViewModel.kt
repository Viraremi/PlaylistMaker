package com.practicum.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.InteractorFavorite
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
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
    private val interactorFavorite: InteractorFavorite,
    private val interactorPlaylist: InteractorPlaylist
) : ViewModel() {

    private val stateFavorite = MutableLiveData<Boolean>()
    fun getStateFavorite(): LiveData<Boolean> = stateFavorite

    fun onClickFavorite(track: Track){
        viewModelScope.launch {
            if (stateFavorite.value == true) {
                stateFavorite.postValue(false)
                interactorFavorite.deleteFromFavorite(track)
            } else {
                stateFavorite.postValue(true)
                interactorFavorite.addToFavorite(track)
            }
        }
    }

    private val currentPlaylists = mutableListOf<Playlist>()
    fun getPlaylists(): List<Playlist> {
        viewModelScope.launch {
            interactorPlaylist.getPlaylists().collect { playlists ->
                currentPlaylists.clear()
                currentPlaylists.addAll(playlists)
            }
        }
        return currentPlaylists
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track, onComplete: (Boolean) -> Unit) {
        if (track.trackId in playlist.tracksList) {
            onComplete(false)
            return
        }
        viewModelScope.launch {
            interactorPlaylist.addTrackToPlaylist(playlist, track)
        }
        onComplete(true)
    }

    private val statePlayerView = MutableLiveData<PlayerViewState>()
    fun getStatePlayerView(): LiveData<PlayerViewState> = statePlayerView

    private var timerJob: Job? = null

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

    fun updateFavoriteStatus(track: Track){
        viewModelScope.launch {
            interactorFavorite.getIDsFavorite().collect { favorite ->
                stateFavorite.postValue(track.trackId in favorite)
            }
        }
    }
}