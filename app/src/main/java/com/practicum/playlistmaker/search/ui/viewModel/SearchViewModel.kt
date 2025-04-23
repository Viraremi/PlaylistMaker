package com.practicum.playlistmaker.search.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.InteractorHistory
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.usecase.GetTracksUseCase
import com.practicum.playlistmaker.search.ui.model.SearchHistoryState
import com.practicum.playlistmaker.search.ui.model.SearchState
import com.practicum.playlistmaker.util.SingleLiveEvent
import com.practicum.playlistmaker.util.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getTracksUseCase: GetTracksUseCase,
    private val historyInteractor: InteractorHistory
): ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val stateRequest = SingleLiveEvent<SearchState>()
    fun getState(): LiveData<SearchState> = stateRequest

    private fun loadData(searchText: String){
        if (searchText.isNotEmpty()){
            stateRequest.value = SearchState.Loading
            getTracksUseCase.execute(
                searchText = searchText,
                consumer = object : Consumer<List<Track>> {
                    override fun consume(data: ConsumerData<List<Track>>) {
                        when (data) {
                            is ConsumerData.Data -> {
                                if (data.value.isEmpty()) {
                                    stateRequest.postValue(SearchState.EmptyError(""))
                                } else {
                                    stateRequest.postValue(SearchState.Content(data.value))
                                }
                            }

                            is ConsumerData.Error -> {
                                stateRequest.postValue(SearchState.ConnectionError(data.msg))
                            }
                        }
                    }
                }
            )
        }
    }

    private val stateHistory = MutableLiveData<SearchHistoryState>()
    fun getStateHistory(): LiveData<SearchHistoryState> = stateHistory

    fun getHistory(): List<Track>{
        val history = historyInteractor.getHistory()
        if (history.isEmpty()){
            stateHistory.value = SearchHistoryState.Empty
        }
        else {
            stateHistory.value = SearchHistoryState.HasValue(history)
        }
        return history
    }

    fun clearHistory(){
        historyInteractor.clear()
        stateHistory.value = SearchHistoryState.Empty
    }

    fun addToHistory(track: Track){
        historyInteractor.add(track)
    }

    fun getTrackId(track: Track): Int?{
        return historyInteractor.getIdByTrack(track)
    }

    private val onSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { searchText ->
        loadData(searchText)
    }

    private var searchJob: Job? = null
    fun searchDebounce(searchText: String){
        onSearchDebounce(searchText)
    }
}