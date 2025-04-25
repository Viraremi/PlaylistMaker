package com.practicum.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.library.data.db.dao.FavoriteDao
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity

@Database(entities = [TrackEntity::class], version = 1)
abstract class AppDB(): RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
}