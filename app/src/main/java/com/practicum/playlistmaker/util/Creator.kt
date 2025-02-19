package com.practicum.playlistmaker.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.RepositoryPlayerImpl
import com.practicum.playlistmaker.player.domain.api.InteractorPlayer
import com.practicum.playlistmaker.player.domain.api.RepositoryPlayer
import com.practicum.playlistmaker.player.domain.interactor.InteractorPlayerImpl
import com.practicum.playlistmaker.search.data.network.ITunesRetrofitNetworkClientImpl
import com.practicum.playlistmaker.search.data.repository.RepositoryHistoryImpl
import com.practicum.playlistmaker.search.data.repository.RepositoryNetworkRetrofitImpl
import com.practicum.playlistmaker.search.domain.interactor.InteractorHistoryImpl
import com.practicum.playlistmaker.search.domain.api.InteractorHistory
import com.practicum.playlistmaker.search.domain.api.RepositoryHistory
import com.practicum.playlistmaker.search.domain.usecase.GetTracksUseCase
import com.practicum.playlistmaker.settings.data.RepositorySettigsImpl
import com.practicum.playlistmaker.settings.domain.Interactor.InteractorSettingsImpl
import com.practicum.playlistmaker.settings.domain.api.InteractorSettings
import com.practicum.playlistmaker.settings.domain.api.RepositorySettings
import com.practicum.playlistmaker.sharing.data.RepositorySharingImpl
import com.practicum.playlistmaker.sharing.domain.api.InteractorSharing
import com.practicum.playlistmaker.sharing.domain.api.RepositorySharing
import com.practicum.playlistmaker.sharing.domain.interactor.InteractorSharingImpl

object Creator {

    const val SP_UI_THEME = "ui_theme"
    const val SP_SEARCH_HISTORY = "search_history"

    private lateinit var application: Application
    val gson = Gson()

    fun initApplication(application: Application){
        Creator.application = application
    }

    //searchLogic

    //historySharedPref
    private fun provideHistorySP(): SharedPreferences{
        return application.getSharedPreferences(SP_SEARCH_HISTORY, Context.MODE_PRIVATE)
    }

    private fun provideRepositoryHistory(): RepositoryHistory {
        return RepositoryHistoryImpl(provideHistorySP(), gson)
    }

    fun provideInteractorHistory(): InteractorHistory {
        return InteractorHistoryImpl(provideRepositoryHistory())
    }

    //tracksRequestUseCase
    private fun provideNetworkClient(): ITunesRetrofitNetworkClientImpl {
        return ITunesRetrofitNetworkClientImpl()
    }

    private fun provideNetworkRepository(): RepositoryNetworkRetrofitImpl {
        return RepositoryNetworkRetrofitImpl(provideNetworkClient())
    }

    fun provideGetTracksUseCase(): GetTracksUseCase {
        return GetTracksUseCase(provideNetworkRepository())
    }

    //playerLogic

    private fun providePlayerRepository(): RepositoryPlayer{
        return RepositoryPlayerImpl()
    }

    fun providePlayerInteractor(): InteractorPlayer{
        return InteractorPlayerImpl(providePlayerRepository())
    }

    //settingsLogic

    private fun provideThemeSP(): SharedPreferences{
        return application.getSharedPreferences(SP_UI_THEME, Context.MODE_PRIVATE)
    }

    private fun provideSettingsRepository(): RepositorySettings{
        return RepositorySettigsImpl(provideThemeSP())
    }

    fun provideSettingsInteractor(): InteractorSettings{
        return InteractorSettingsImpl(provideSettingsRepository())
    }

    //sharingLogic

    private fun provideSharingRepository() : RepositorySharing{
        return RepositorySharingImpl(application)
    }

    fun provideSharingInteractor() : InteractorSharing{
        return InteractorSharingImpl(provideSharingRepository())
    }
}