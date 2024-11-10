package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class ResponseTracks(@SerializedName("results") val foundTracks: List<Track>)
