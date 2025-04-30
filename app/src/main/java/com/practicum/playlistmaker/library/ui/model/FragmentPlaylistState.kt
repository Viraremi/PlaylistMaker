package com.practicum.playlistmaker.library.ui.model

import com.practicum.playlistmaker.library.domain.model.Playlist

interface FragmentPlaylistState {
    data object EMPTY: FragmentPlaylistState
    data class CONTENT(val data: List<Playlist>): FragmentPlaylistState
}