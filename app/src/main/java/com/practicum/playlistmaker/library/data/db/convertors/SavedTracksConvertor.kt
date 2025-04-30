package com.practicum.playlistmaker.library.data.db.convertors

import com.practicum.playlistmaker.library.data.db.entity.SavedTracksEntity
import com.practicum.playlistmaker.search.domain.model.Track

class SavedTracksConvertor {

    fun map(entity: SavedTracksEntity): Track {
        return Track(
            entity.trackId,
            entity.trackName,
            entity.artistName,
            entity.trackTimeMillis,
            entity.artworkUrl100,
            entity.collectionName,
            entity.releaseDate,
            entity.primaryGenreName,
            entity.country,
            entity.previewUrl
        )
    }

    fun map(entity: Track): SavedTracksEntity {
        return SavedTracksEntity(
            entity.trackId,
            entity.trackName,
            entity.artistName,
            entity.trackTimeMillis,
            entity.artworkUrl100,
            entity.collectionName,
            entity.releaseDate,
            entity.primaryGenreName,
            entity.country,
            entity.previewUrl
        )
    }
}
