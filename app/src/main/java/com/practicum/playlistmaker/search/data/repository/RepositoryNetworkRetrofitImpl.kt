package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.library.data.db.AppDB
import com.practicum.playlistmaker.search.data.model.ResponseTracks
import com.practicum.playlistmaker.search.data.network.ITunesNetworkClient
import com.practicum.playlistmaker.search.domain.api.RepositoryNetwork
import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RepositoryNetworkRetrofitImpl(
    private val networkClient: ITunesNetworkClient,
    private val db: AppDB
) : RepositoryNetwork {

    override fun getTracks(currency: String): Flow<Resource<List<Track>>> = flow {
        val tracksResponse = networkClient.getTracks(currency)
        when (tracksResponse.resultCode) {
            200 -> {
                val favoriteIDs = db.favoriteDao().getIDsFavorite()
                //Redundant 'with' call Почему??
                with(tracksResponse as ResponseTracks){
                    val tracks = tracksResponse.foundTracks.map{
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl,
                            it.trackId in favoriteIDs
                        )
                    }
                    emit(Resource.Success(tracks))
                }
            }
            500 -> {
                emit(Resource.Error("Server error"))
            }
            else -> {
                emit(Resource.Error("Network error"))
            }
        }
    }.flowOn(Dispatchers.IO)
}