package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.repository.RepositoryHistoryImpl
import com.practicum.playlistmaker.domain.InteractorHistoryImpl
import com.practicum.playlistmaker.domain.api.InteractorHistory
import com.practicum.playlistmaker.domain.api.RepositoryHistory

object Creator {
    private lateinit var application: Application
    val gson = Gson()

    fun initApplication(application: Application){
        this.application = application
    }

    private fun provideSharedPreferences(): SharedPreferences{
        return application.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    }

    private fun provideRepositoryHistory(): RepositoryHistory{
        return RepositoryHistoryImpl(provideSharedPreferences(), gson)
    }

    fun provideInteractorHistory(): InteractorHistory {
        return InteractorHistoryImpl(provideRepositoryHistory())
    }
}