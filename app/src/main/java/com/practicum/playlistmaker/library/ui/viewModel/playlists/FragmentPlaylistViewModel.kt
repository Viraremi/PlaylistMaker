package com.practicum.playlistmaker.library.ui.viewModel.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentPlaylistState
import kotlinx.coroutines.launch

class FragmentPlaylistViewModel(
    private val interactor: InteractorPlaylist,
    private val gson: Gson
): ViewModel() {

    private val state = MutableLiveData<FragmentPlaylistState>()
    fun getState(): LiveData<FragmentPlaylistState> = state

    init {
        getPlaylists()
    }

    fun getPlaylists(){
        viewModelScope.launch {
            interactor.getPlaylists().collect { playlists ->
                if (playlists.isEmpty()) {
                    state.postValue(FragmentPlaylistState.EMPTY)
                }
                else {
                    state.postValue(FragmentPlaylistState.CONTENT(playlists))
                }
            }
        }
    }

    fun playlistToJson(playlist: Playlist): String {
        return gson.toJson(playlist)
    }
}