package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.SavedTracksEntity

@Dao
interface SavedTracksDao {

    @Query("SELECT * FROM saved_tracks WHERE trackId IN (:list)")
    fun getTrackList(list: List<Int>): List<SavedTracksEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSavedTrack(track: SavedTracksEntity)

    @Delete
    fun deleteSavedTrack(track: SavedTracksEntity)
}