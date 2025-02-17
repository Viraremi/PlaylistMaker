package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.model.NetworkResponse

interface ITunesNetworkClient {
    fun getTracks(currency: String): NetworkResponse
}