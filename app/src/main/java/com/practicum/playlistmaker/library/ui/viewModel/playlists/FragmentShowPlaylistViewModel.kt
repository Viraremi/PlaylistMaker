package com.practicum.playlistmaker.library.ui.viewModel.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentShowPlaylistState
import com.practicum.playlistmaker.library.ui.model.LibraryViewState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class FragmentShowPlaylistViewModel(
    private val interactorPlaylist: InteractorPlaylist
) : ViewModel() {

    private val state = MutableLiveData<FragmentShowPlaylistState>()
    fun getState(): LiveData<FragmentShowPlaylistState> = state

    private val currentTracks: MutableList<Track> = mutableListOf()
    fun getTracks(playlist: Playlist): List<Track> {
        viewModelScope.launch {
            interactorPlaylist.getTracksFromPlaylist(playlist).collect { tracks->
                currentTracks.clear()
                currentTracks.addAll(tracks)
            }
        }
        return currentTracks
    }

    init {
        getPlaylistById(1)
    }

    fun getPlaylistById(id: Int) {
        viewModelScope.launch {
            currentPlaylist = interactorPlaylist.getPlaylistById(id)
        }
    }

    fun returnPlaylist(): Playlist{
        return currentPlaylist!!
    }
}