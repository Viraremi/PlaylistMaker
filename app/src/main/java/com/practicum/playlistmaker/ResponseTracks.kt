package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.domain.model.Track

data class ResponseTracks(@SerializedName("results") val foundTracks: List<Track>)
