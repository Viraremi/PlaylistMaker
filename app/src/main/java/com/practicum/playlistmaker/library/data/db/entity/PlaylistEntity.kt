package com.practicum.playlistmaker.library.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "img_path")
    val imgPath: String,
    @ColumnInfo(name = "tracks_list")
    val tracksList: String,
    @ColumnInfo(name = "tracks_count")
    val tracksCount: Int
)