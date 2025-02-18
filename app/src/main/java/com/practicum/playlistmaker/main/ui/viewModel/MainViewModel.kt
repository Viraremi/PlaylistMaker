package com.practicum.playlistmaker.main.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.main.ui.model.MainViewState

class MainViewModel(): ViewModel() {

    val state = MutableLiveData<MainViewState>()
    fun getState(): LiveData<MainViewState> = state

    fun showSearch(){
        state.value = MainViewState.SEARCH
    }

    fun showLibrary(){
        state.value = MainViewState.LIBRARY
    }

    fun showSettings(){
        state.value = MainViewState.SETTINGS
    }
}