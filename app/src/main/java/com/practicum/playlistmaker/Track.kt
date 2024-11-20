package com.practicum.playlistmaker

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackId: Int, // Уникальный id трека
    val collectionName: String, // Альбом
    val releaseDate: Int, // Год релиза
    val primaryGenreName: String, // Жанр
    val country: String // Страна исполнителя
) : Serializable{
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun timeValidFormat() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
}
