package com.practicum.playlistmaker.library.ui.model

import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track

sealed interface FragmentShowPlaylistState {
    data class CONTENT(
        val playlist: Playlist,
        val tracks: List<Track>,
        val countString: String,
        val timeString: String
    ): FragmentShowPlaylistState
}