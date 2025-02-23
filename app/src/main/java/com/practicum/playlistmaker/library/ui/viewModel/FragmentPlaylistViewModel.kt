package com.practicum.playlistmaker.library.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.ui.model.FragmentPlaylistState

class FragmentPlaylistViewModel: ViewModel() {

    private val state = MutableLiveData<FragmentPlaylistState>()
    fun getState(): LiveData<FragmentPlaylistState> = state
}