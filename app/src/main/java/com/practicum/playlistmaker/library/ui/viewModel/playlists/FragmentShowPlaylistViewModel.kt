package com.practicum.playlistmaker.library.ui.viewModel.playlists

import android.util.Log
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
import com.practicum.playlistmaker.sharing.domain.api.InteractorSharing
import com.practicum.playlistmaker.util.StringFormatter
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FragmentShowPlaylistViewModel(
    private val interactorPlaylist: InteractorPlaylist,
    private val interactorSharing: InteractorSharing,
    private val gson: Gson
) : ViewModel() {

    private val state = MutableLiveData<FragmentShowPlaylistState>()
    fun getState(): LiveData<FragmentShowPlaylistState> = state

    fun loadContent(playlist: Playlist) {
        viewModelScope.launch {
            Log.i("my_info", "data loaded start")

            val currentPlaylist: Deferred<Playlist> = async(Dispatchers.IO) {
                var result: Playlist? = null
                interactorPlaylist.getPlaylists().collect { playlists ->
                    result = playlists.firstOrNull { it.id == playlist.id }
                }
                return@async result!!
            }

            val tracks: Deferred<List<Track>> = async(Dispatchers.IO) {
                var result = listOf<Track>()
                interactorPlaylist.getTracksFromPlaylist(currentPlaylist.await()).collect { tracks ->
                    result = tracks
                }
                return@async result
            }

            state.postValue(
                FragmentShowPlaylistState.CONTENT(
                    currentPlaylist.await(),
                    tracks.await(),
                    getCountString(playlist),
                    getTimeString(tracks.await())
                )
            )

            Log.i("my_info", "data loaded end")
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

    fun deleteTrackFromPlaylist(playlist: Playlist, track: Track): Job {
        val job = viewModelScope.launch {
            launch {
                Log.i("my_info", "track deleted start")
                val isExistInOther: Deferred<Boolean> = async(Dispatchers.IO) {
                    var flag = false
                    interactorPlaylist.getPlaylists().collect { list ->
                        for (item in list) {
                            if (track.trackId in item.tracksList) {
                                flag = true
                                return@collect
                            }
                        }
                    }
                    return@async flag
                }

                interactorPlaylist.deleteTrackFromPlaylist(playlist, track, isExistInOther.await())
                Log.i("my_info", "track deleted end")
            }.join()

            launch {
                loadContent(playlist)
            }.join()
        }
        return job
    }

    var shareMsg = ""

    fun generateMsg(playlist: Playlist, tracks: List<Track>) {

        shareMsg += "Название: ${playlist.name}\n" +
                "Описание: ${playlist.description}\n" +
                "${StringFormatter.countString(playlist.tracksCount)}\n"

        tracks.forEachIndexed { index, track ->
            shareMsg += "${index+1}. ${track.artistName} - ${track.trackName} " +
                    "(${StringFormatter.getValidTimeFormat(track.trackTimeMillis)})\n"
        }
    }

    fun sharePlaylist() {
        interactorSharing.shareApp(shareMsg)
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            Log.i("my_info", "playlist deleted start")
            val tracks: Deferred<List<Track>> = async(Dispatchers.IO) {
                var result: List<Track> = listOf()
                interactorPlaylist.getTracksFromPlaylist(playlist).collect { list ->
                    result = list
                }
                return@async result
            }

            launch {
                interactorPlaylist.deletePlaylist(playlist)
            }.join()

            for (item in tracks.await()) {
                val job = deleteTrackFromPlaylist(playlist, item)
                job.join()
            }
            Log.i("my_info", "playlist deleted end")
        }
    }
}