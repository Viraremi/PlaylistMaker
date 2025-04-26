package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.library.data.repository.RepositoryFavoriteImpl
import com.practicum.playlistmaker.library.domain.api.RepositoryFavorite
import com.practicum.playlistmaker.player.data.RepositoryPlayerImpl
import com.practicum.playlistmaker.player.domain.api.RepositoryPlayer
import com.practicum.playlistmaker.search.data.repository.RepositoryHistoryImpl
import com.practicum.playlistmaker.search.data.repository.RepositoryNetworkRetrofitImpl
import com.practicum.playlistmaker.search.domain.api.RepositoryHistory
import com.practicum.playlistmaker.search.domain.api.RepositoryNetwork
import com.practicum.playlistmaker.settings.data.RepositorySettingsImpl
import com.practicum.playlistmaker.settings.domain.api.RepositorySettings
import com.practicum.playlistmaker.sharing.data.RepositorySharingImpl
import com.practicum.playlistmaker.sharing.domain.api.RepositorySharing
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val SP_SEARCH_HISTORY = "search_history"
const val SP_UI_THEME = "ui_theme"

val repositoryModule = module {

    single { Gson() }

    single<RepositoryHistory> {
        RepositoryHistoryImpl(
            androidContext().getSharedPreferences(SP_SEARCH_HISTORY, Context.MODE_PRIVATE),
            get(),
            get()
        )
    }

    single<RepositoryNetwork> {
        RepositoryNetworkRetrofitImpl(get(), get())
    }

    single { MediaPlayer() }

    single<RepositoryPlayer> {
        RepositoryPlayerImpl(
            get()
        )
    }

    single<RepositorySettings> {
        RepositorySettingsImpl(
            androidContext().getSharedPreferences(SP_UI_THEME, Context.MODE_PRIVATE)
        )
    }

    single<RepositorySharing> {
        RepositorySharingImpl(androidContext())
    }

    single<RepositoryFavorite> {
        RepositoryFavoriteImpl(get(), get())
    }
}