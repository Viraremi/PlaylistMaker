package com.practicum.playlistmaker.library.ui.viewModel.playlists

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentNewPlaylistState
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class FragmentNewPlaylistViewModel(
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
}