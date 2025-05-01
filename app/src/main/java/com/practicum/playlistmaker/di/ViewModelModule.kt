package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.ui.viewModel.favorite.FragmentFavoriteViewModel
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentPlaylistViewModel
import com.practicum.playlistmaker.library.ui.viewModel.LibraryViewModel
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentNewPlaylistViewModel
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentShowPlaylistViewModel
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import com.practicum.playlistmaker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        PlayerViewModel(get(), get(), get(), get())
    }

    viewModel {
        LibraryViewModel()
    }

    viewModel {
        FragmentFavoriteViewModel(get())
    }

    viewModel {
        FragmentPlaylistViewModel(get(), get())
    }

    viewModel {
        FragmentNewPlaylistViewModel(get(), get())
    }

    viewModel {
        FragmentShowPlaylistViewModel(get(), get(), get())
    }
}