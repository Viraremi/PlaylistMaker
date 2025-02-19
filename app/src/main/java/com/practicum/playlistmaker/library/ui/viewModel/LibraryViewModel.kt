package com.practicum.playlistmaker.library.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.ui.model.LibraryViewState

class LibraryViewModel: ViewModel() {

    private val state = MutableLiveData<LibraryViewState>()
    fun getState(): LiveData<LibraryViewState> = state

    init {
        state.value = LibraryViewState.NOTHING
    }
}