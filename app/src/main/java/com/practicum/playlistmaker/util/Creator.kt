package com.practicum.playlistmaker.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.network.ITunesRetrofitNetworkClientImpl
import com.practicum.playlistmaker.search.data.repository.RepositoryHistoryImpl
import com.practicum.playlistmaker.search.data.repository.RepositoryNetworkRetrofitImpl
import com.practicum.playlistmaker.search.domain.interactor.InteractorHistoryImpl
import com.practicum.playlistmaker.search.domain.api.InteractorHistory
import com.practicum.playlistmaker.search.domain.api.RepositoryHistory
import com.practicum.playlistmaker.search.domain.usecase.GetTracksUseCase

object Creator {

    //searchLogic

    private lateinit var application: Application
    val gson = Gson()

    fun initApplication(application: Application){
        Creator.application = application
    }

    //historySharedPref

    private fun provideSharedPreferences(): SharedPreferences{
        return application.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    }

    private fun provideRepositoryHistory(): RepositoryHistory {
        return RepositoryHistoryImpl(provideSharedPreferences(), gson)
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

    //settingsLogic

    //sharingLogic
}