package com.practicum.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_tracks")
data class SavedTracksEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Альбом
    val releaseDate: String, // Год релиза
    val primaryGenreName: String, // Жанр
    val country: String, // Страна исполнителя
    val previewUrl: String //Ссылка на превью(первые 30 сек) трека
)