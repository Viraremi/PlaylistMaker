package com.practicum.playlistmaker.library.ui.model

import android.net.Uri
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track

sealed interface FragmentShowPlaylistState {
    data class CONTENT(
        val playlist: Playlist,
        val tracks: List<Track>,
        val countString: String,
        val timeString: String
    ): FragmentShowPlaylistState
    data object EMPTY: FragmentShowPlaylistState
    data class HAS_IMAGE(val uri: Uri): FragmentShowPlaylistState
    data class EDIT(val playlist: Playlist): FragmentShowPlaylistState
}