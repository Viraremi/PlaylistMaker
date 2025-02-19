package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.model.NetworkResponse

class ITunesRetrofitNetworkClientImpl(
    private val tracksAPI: ITunesAPI
): ITunesNetworkClient {

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