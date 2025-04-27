package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.model.Track

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_tracks_table")
    fun getFavorite(): List<TrackEntity>

    @Query("SELECT trackId FROM favorite_tracks_table")
    fun getIDsFavorite(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(track: TrackEntity)

    @Delete
    fun deleteFavorite(track: TrackEntity)
}