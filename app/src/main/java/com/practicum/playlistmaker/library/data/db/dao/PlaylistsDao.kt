package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.search.domain.model.Track

@Dao
interface PlaylistsDao {

    @Query("SELECT * FROM playlists_table")
    fun getPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Delete
    fun deletePlaylists(playlist: PlaylistEntity)

    @Query("UPDATE playlists_table SET tracks_list = :trackListJson WHERE id = :playlistId")
    fun updateTrackList(playlistId: Int, trackListJson: String)
}