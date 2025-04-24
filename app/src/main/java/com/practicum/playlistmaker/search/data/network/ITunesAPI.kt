package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.model.ResponseTracks
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    suspend fun getTrack(@Query("term") text: String) : ResponseTracks
}