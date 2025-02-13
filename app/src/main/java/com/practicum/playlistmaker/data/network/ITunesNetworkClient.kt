package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.model.NetworkResponse

interface ITunesNetworkClient {
    fun getTracks(currency: String): NetworkResponse
}