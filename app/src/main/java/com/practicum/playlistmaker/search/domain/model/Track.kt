package com.practicum.playlistmaker.search.domain.model

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Альбом
    val releaseDate: String, // Год релиза
    val primaryGenreName: String, // Жанр
    val country: String, // Страна исполнителя
    val previewUrl: String //Ссылка на превью(первые 30 сек) трека
){
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
