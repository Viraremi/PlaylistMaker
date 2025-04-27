package com.practicum.playlistmaker.library.ui.viewModel.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.ui.model.FragmentPlaylistState

class FragmentPlaylistViewModel(
    private val interactor: InteractorPlaylist
): ViewModel() {

    private val state = MutableLiveData<FragmentPlaylistState>()
    fun getState(): LiveData<FragmentPlaylistState> = state
}