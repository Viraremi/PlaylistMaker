package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.domain.api.InteractorFavorite
import com.practicum.playlistmaker.library.domain.interactror.InteractorFavoriteImpl
import com.practicum.playlistmaker.player.domain.api.InteractorPlayer
import com.practicum.playlistmaker.player.domain.interactor.InteractorPlayerImpl
import com.practicum.playlistmaker.search.domain.api.InteractorHistory
import com.practicum.playlistmaker.search.domain.interactor.InteractorHistoryImpl
import com.practicum.playlistmaker.search.domain.usecase.GetTracksUseCase
import com.practicum.playlistmaker.settings.domain.Interactor.InteractorSettingsImpl
import com.practicum.playlistmaker.settings.domain.api.InteractorSettings
import com.practicum.playlistmaker.sharing.domain.api.InteractorSharing
import com.practicum.playlistmaker.sharing.domain.interactor.InteractorSharingImpl
import org.koin.dsl.module

val interactorModule = module {

    single<InteractorHistory> {
        InteractorHistoryImpl(get())
    }

    single {
        GetTracksUseCase(get())
    }

    single<InteractorPlayer> {
        InteractorPlayerImpl(get())
    }

    single<InteractorSettings> {
        InteractorSettingsImpl(get())
    }

    single<InteractorSharing> {
        InteractorSharingImpl(get())
    }

    single<InteractorFavorite> {
        InteractorFavoriteImpl(get())
    }
}