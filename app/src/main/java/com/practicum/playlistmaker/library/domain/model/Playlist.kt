package com.practicum.playlistmaker.library.domain.model

data class Playlist(
    val name: String,
    val description: String,
    val imgPath: String,
    val tracksList: List<Int>,
    val tracksCount: Int
)