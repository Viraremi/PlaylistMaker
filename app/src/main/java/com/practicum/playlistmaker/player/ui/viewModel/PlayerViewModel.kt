package com.practicum.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.ui.model.PlayerState

class PlayerViewModel() : ViewModel() {

    private val state = MutableLiveData<PlayerState>()

    fun getState(): LiveData<PlayerState> = state

}