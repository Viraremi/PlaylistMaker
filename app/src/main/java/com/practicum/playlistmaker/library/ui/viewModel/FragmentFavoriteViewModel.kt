package com.practicum.playlistmaker.library.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.ui.model.FragmentFavoriteState

class FragmentFavoriteViewModel: ViewModel() {

    private val state = MutableLiveData<FragmentFavoriteState>()
    fun getState(): LiveData<FragmentFavoriteState> = state
}