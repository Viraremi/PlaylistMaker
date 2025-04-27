package com.practicum.playlistmaker.library.ui.viewModel.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.ui.model.FragmentNewPlaylistState

class FragmentNewPlaylistViewModel: ViewModel() {

    private val state = MutableLiveData<FragmentNewPlaylistState>()
    fun getState(): LiveData<FragmentNewPlaylistState> = state

    init {
        state.value = FragmentNewPlaylistState.NOTHING
    }
}