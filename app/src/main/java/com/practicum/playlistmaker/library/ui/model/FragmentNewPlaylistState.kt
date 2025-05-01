package com.practicum.playlistmaker.library.ui.model

import android.net.Uri
import com.practicum.playlistmaker.library.domain.model.Playlist

interface FragmentNewPlaylistState {
    data object EMPTY: FragmentNewPlaylistState
    data class HAS_IMAGE(val uri: Uri): FragmentNewPlaylistState
    data class EDIT(val playlist: Playlist): FragmentNewPlaylistState
}