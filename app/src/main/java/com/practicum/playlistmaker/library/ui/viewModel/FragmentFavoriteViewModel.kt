package com.practicum.playlistmaker.library.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.InteractorFavorite
import com.practicum.playlistmaker.library.ui.model.FragmentFavoriteState
import kotlinx.coroutines.launch

class FragmentFavoriteViewModel(
    private val interactorFavorite: InteractorFavorite
): ViewModel() {

    private val state = MutableLiveData<FragmentFavoriteState>()
    fun getState(): LiveData<FragmentFavoriteState> = state

    init {
        getFavorite()
    }

    fun getFavorite(){
        viewModelScope.launch {
            interactorFavorite.getFavorite().collect { tracks ->
                if (tracks.isEmpty()){
                    state.postValue(FragmentFavoriteState.Empty)
                }
                else {
                    state.postValue(FragmentFavoriteState.Content(tracks))
                }
            }
        }
    }
}