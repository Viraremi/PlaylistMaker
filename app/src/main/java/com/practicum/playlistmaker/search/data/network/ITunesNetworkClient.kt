package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.model.NetworkResponse

interface ITunesNetworkClient {
    suspend fun getTracks(currency: String): NetworkResponse
}