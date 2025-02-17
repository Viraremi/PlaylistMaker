package com.practicum.playlistmaker.search.ui.viewModel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.search.ui.activity.SearchActivity.Companion
import com.practicum.playlistmaker.search.ui.model.SearchHistoryState
import com.practicum.playlistmaker.search.ui.model.SearchState
import com.practicum.playlistmaker.util.Creator

class SearchViewModel: ViewModel() {

    companion object {
        private const val REQUECT_KEY = "REQUEST_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val getTracksUseCase = Creator.provideGetTracksUseCase()
    private val historyInteractor = Creator.provideInteractorHistory()
    private val handler = Handler(Looper.getMainLooper())

    private val stateRequest = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = stateRequest

    private val stateHistory = MutableLiveData<SearchHistoryState>()
    fun getStateHistory(): LiveData<SearchHistoryState> = stateHistory

    fun loadData(searchText: String){
        stateRequest.value = SearchState.Loading
        getTracksUseCase.execute(
            searchText = searchText,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data){
                        is ConsumerData.Data -> {
                            if (data.value.isEmpty()){
                                stateRequest.postValue(SearchState.EmptyError(""))
                            }
                            else {
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

    fun searchDebounce(searchText: String){
        handler.removeCallbacksAndMessages(REQUECT_KEY)
        val searchRunnable = Runnable { if (searchText.isNotEmpty()) loadData(searchText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            REQUECT_KEY,
            postTime,
        )
    }

    private var isClickAllowed = true
    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}