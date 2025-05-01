package com.practicum.playlistmaker.library.ui.viewModel.playlists

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentNewPlaylistState
import kotlinx.coroutines.launch

class FragmentNewPlaylistViewModel(
    val gson: Gson,
    private val interactor: InteractorPlaylist
): ViewModel() {

    private val state = MutableLiveData<FragmentNewPlaylistState>()
    fun getState(): LiveData<FragmentNewPlaylistState> = state

    init {
        state.postValue(FragmentNewPlaylistState.EMPTY)
    }

    fun createOrUpdatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactor.addPlaylist(playlist)
        }
    }

    fun setImage(uri: Uri){
        state.postValue(FragmentNewPlaylistState.HAS_IMAGE(uri))
    }

    fun playlistFromJson(json: String): Playlist {
        return gson.fromJson(json, object : TypeToken<Playlist>() {}.type)
    }

    fun toEditMode(playlist: Playlist) {
        state.postValue(FragmentNewPlaylistState.EDIT(playlist))
    }
}