package com.practicum.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.library.data.db.dao.FavoriteDao
import com.practicum.playlistmaker.library.data.db.dao.PlaylistsDao
import com.practicum.playlistmaker.library.data.db.dao.SavedTracksDao
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.SavedTracksEntity
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity

@Database(
    entities = [TrackEntity::class, PlaylistEntity::class, SavedTracksEntity::class],
    version = 1
)
abstract class AppDB(): RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    abstract fun playlistDao(): PlaylistsDao

    abstract fun savedTracksDao(): SavedTracksDao
}