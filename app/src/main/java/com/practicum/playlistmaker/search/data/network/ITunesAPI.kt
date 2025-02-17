package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.model.ResponseTracks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    fun getTrack(@Query("term") text: String) : Call<ResponseTracks>
}