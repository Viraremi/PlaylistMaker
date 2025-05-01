package com.practicum.playlistmaker.library.ui.viewModel.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentShowPlaylistState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.StringFormatter
import kotlinx.coroutines.launch

class FragmentShowPlaylistViewModel(
    private val interactorPlaylist: InteractorPlaylist,
    private val gson: Gson
) : ViewModel() {

    private val state = MutableLiveData<FragmentShowPlaylistState>()
    fun getState(): LiveData<FragmentShowPlaylistState> = state

    fun loadContent(playlist: Playlist) {
        viewModelScope.launch {
            interactorPlaylist.getTracksFromPlaylist(playlist).collect { tracks->
                state.postValue(
                    FragmentShowPlaylistState.CONTENT(
                        playlist,
                        tracks,
                        getCountString(playlist),
                        getTimeString(tracks)
                    )
                )
            }
        }
    }

    fun playlistFromJson(json: String): Playlist {
        return gson.fromJson(json, object : TypeToken<Playlist>() {}.type)
    }

    private fun getTimeString(tracks: List<Track>): String{
        var sumTime = 0L
        for (track in tracks) {
            sumTime += track.trackTimeMillis
        }
        return StringFormatter.minuteString(sumTime/60000)
    }

    private fun getCountString(playlist: Playlist): String{
        return StringFormatter.countString(playlist.tracksCount)
    }
}