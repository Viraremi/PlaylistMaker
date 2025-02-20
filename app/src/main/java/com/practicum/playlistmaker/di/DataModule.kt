package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.data.network.ITunesAPI
import com.practicum.playlistmaker.search.data.network.ITunesNetworkClient
import com.practicum.playlistmaker.search.data.network.ITunesRetrofitNetworkClientImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://itunes.apple.com"

val dataModule = module {

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single<ITunesNetworkClient> {
        ITunesRetrofitNetworkClientImpl(get())
    }
}