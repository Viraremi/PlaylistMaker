package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.model.NetworkResponse

class ITunesRetrofitNetworkClientImpl(
    private val tracksAPI: ITunesAPI
): ITunesNetworkClient {

    override suspend fun getTracks(currency: String): NetworkResponse {
        return try {
            val response = tracksAPI.getTrack(currency)
            response.apply { resultCode = 200 }
        } catch (ex: Exception){
            NetworkResponse().apply { resultCode = 500 }
        }
    }
}