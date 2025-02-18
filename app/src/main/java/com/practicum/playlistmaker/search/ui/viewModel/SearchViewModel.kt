package com.practicum.playlistmaker.search.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.model.SearchHistoryState
import com.practicum.playlistmaker.search.ui.model.SearchState
import com.practicum.playlistmaker.util.Creator

class SearchViewModel: ViewModel() {

    private val getTracksUseCase = Creator.provideGetTracksUseCase()
    private val historyInteractor = Creator.provideInteractorHistory()

    private val stateRequest = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = stateRequest

    fun loadData(searchText: String){
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

    fun clear(){
        historyInteractor.clear()
        stateHistory.value = SearchHistoryState.Empty
    }

    fun add(track: Track){
        historyInteractor.add(track)
    }
}