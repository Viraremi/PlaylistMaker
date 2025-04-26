package com.practicum.playlistmaker.library.data.repository

import com.practicum.playlistmaker.library.data.db.AppDB
import com.practicum.playlistmaker.library.data.db.DBConvertor
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.library.domain.api.RepositoryFavorite
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RepositoryFavoriteImpl(
    private val db: AppDB,
    private val convertor: DBConvertor
): RepositoryFavorite {

    override suspend fun addToFavorite(track: Track) {
        withContext(Dispatchers.IO) {
            db.favoriteDao().insertFavorite(convertor.map(track))
        }
    }

    override suspend fun deleteFromFavorite(track: Track) {
        withContext(Dispatchers.IO) {
            db.favoriteDao().deleteFavorite(convertor.map(track))
        }
    }

    override suspend fun getFavorite(): Flow<List<Track>> = flow {
        val favorites = db.favoriteDao().getFavorite()
        emit(convertFromEntity(favorites))
    }.flowOn(Dispatchers.IO)

    private fun convertFromEntity(tracks: List<TrackEntity>): List<Track>{
        return tracks.map { track -> convertor.map(track) }
    }
}