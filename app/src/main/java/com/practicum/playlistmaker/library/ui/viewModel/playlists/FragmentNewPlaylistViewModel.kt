package com.practicum.playlistmaker.library.ui.viewModel.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentNewPlaylistState
import kotlinx.coroutines.launch

class FragmentNewPlaylistViewModel(
    private val interactor: InteractorPlaylist
): ViewModel() {

    private val state = MutableLiveData<FragmentNewPlaylistState>()
    fun getState(): LiveData<FragmentNewPlaylistState> = state

    init {
        state.value = FragmentNewPlaylistState.NOTHING
    }

    fun createOrUpdatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactor.addPlaylist(playlist)
        }
    }
}