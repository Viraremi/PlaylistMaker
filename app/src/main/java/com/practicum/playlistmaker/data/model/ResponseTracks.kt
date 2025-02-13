package com.practicum.playlistmaker.data.model

import com.google.gson.annotations.SerializedName

data class ResponseTracks(@SerializedName("results") val foundTracks: List<TrackDTO>): NetworkResponse()
