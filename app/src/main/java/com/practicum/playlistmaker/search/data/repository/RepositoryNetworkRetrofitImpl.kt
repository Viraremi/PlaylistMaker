package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.model.ResponseTracks
import com.practicum.playlistmaker.search.data.network.ITunesNetworkClient
import com.practicum.playlistmaker.search.domain.api.RepositoryNetwork
import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track

class RepositoryNetworkRetrofitImpl(
    private val networkClient: ITunesNetworkClient
) : RepositoryNetwork {

    override fun getTracks(currency: String): Resource<List<Track>> {
        val tracksResponse = networkClient.getTracks(currency)

        return if (tracksResponse is ResponseTracks) {
            val tracks = tracksResponse.foundTracks.map{
                Track(
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
            Resource.Success(tracks)
        } else {
            Resource.Error("Сетевая ошибка")
        }
    }
}