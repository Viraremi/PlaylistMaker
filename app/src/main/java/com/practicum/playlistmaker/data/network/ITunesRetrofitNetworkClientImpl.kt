package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.model.NetworkResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesRetrofitNetworkClientImpl: ITunesNetworkClient {

    val BASE_URL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val tracksAPI = retrofit.create(ITunesAPI::class.java)

    override fun getTracks(currency: String): NetworkResponse {
        return try {
            val response = tracksAPI.getTrack(currency).execute()
            val networkResponse = response.body() ?: NetworkResponse()
            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception){
            NetworkResponse().apply { resultCode = -1 }
        }
    }
}