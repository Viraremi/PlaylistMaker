package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.api.InteractorHistory
import com.practicum.playlistmaker.search.domain.api.RepositoryHistory
import com.practicum.playlistmaker.search.domain.model.Track

class InteractorHistoryImpl(
    private val repository: RepositoryHistory
): InteractorHistory {
    override fun getHistory(): MutableList<Track> {
        return repository.getHistory()
    }

    override fun add(item: Track) {
        repository.add(item)
    }

    override fun clear() {
        repository.clear()
    }
}