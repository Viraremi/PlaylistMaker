package com.practicum.playlistmaker.search.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.model.SearchState
import com.practicum.playlistmaker.util.Creator

class SearchViewModel: ViewModel() {
    private val getTracksUseCase = Creator.provideGetTracksUseCase()

    private val state = MutableLiveData<SearchState>()

    fun getState(): LiveData<SearchState> = state

    fun loadData(searchText: String){
        state.value = SearchState.Loading

        getTracksUseCase.execute(
            searchText = searchText,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data){
                        is ConsumerData.Data -> {
                            if (data.value.isEmpty()){
                                state.postValue(SearchState.EmptyError(""))
                            }
                            else {
                                state.postValue(SearchState.Content(data.value))
                            }
                        }
                        is ConsumerData.Error -> {
                            state.postValue(SearchState.ConnectionError(data.msg))
                        }
                    }
                }
            }
        )
    }
}