package com.practicum.playlistmaker.library.ui.model

import android.net.Uri

interface FragmentNewPlaylistState {
    data object EMPTY: FragmentNewPlaylistState
    data class HAS_IMAGE(val uri: Uri): FragmentNewPlaylistState
}