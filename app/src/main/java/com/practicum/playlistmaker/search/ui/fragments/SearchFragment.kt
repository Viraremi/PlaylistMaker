package com.practicum.playlistmaker.search.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.ui.fragment.FragmentPlayer
import com.practicum.playlistmaker.search.ui.model.SearchHistoryState
import com.practicum.playlistmaker.search.ui.model.SearchState
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import com.practicum.playlistmaker.util.RootActivity
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

    private lateinit var searchResultsAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private var searchInstanceState = ""
    private var searchText = ""
    private var searchResultsAdapterList = mutableListOf<Track>()
    private var historyAdapterList = mutableListOf<Track>()
    private lateinit var onClickDebounce: (Int) -> Unit

    val viewModel by viewModel<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        refreshHistory()
        super.onStart()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickDebounce = debounce<Int>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { trackId ->
            (activity as RootActivity).animateBottomNavigationView()
            val bundle = Bundle().apply { putInt(FragmentPlayer.TRACK_ID, trackId) }
            findNavController().navigate(R.id.action_searchFragment_to_playerFragment, bundle)
        }

        historyAdapterList.addAll(viewModel.getHistory())

        initListeners()

        searchResultsAdapter = TrackAdapter(searchResultsAdapterList, longClick = {/**/},
            click = { track ->
                viewModel.addToHistory(track)
                onClickDebounce(viewModel.getTrackId(track)!!)
            }
        )
        binding.searchResultRecycler.adapter = searchResultsAdapter
        binding.searchResultRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        historyAdapter = TrackAdapter(historyAdapterList, longClick = {/**/},
            click = { track ->
                onClickDebounce(viewModel.getTrackId(track)!!)
            }
        )
        historyAdapter.notifyDataSetChanged()
        binding.searchHistoryRecycler.adapter = historyAdapter
        binding.searchHistoryRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getState().observe(viewLifecycleOwner){ state ->
            when(state){
                is SearchState.ConnectionError -> {
                    searchResultsAdapterList.clear()
                    showErrorConnection()
                }
                is SearchState.EmptyError -> {
                    searchResultsAdapterList.clear()
                    showErrorEmptyReponse()
                }
                is SearchState.Content -> {
                    searchResultsAdapterList.clear()
                    searchResultsAdapterList.addAll(state.data)
                    searchResultsAdapter.notifyDataSetChanged()
                    showResult()
                }
                SearchState.Loading -> showLoading()
            }
        }

        viewModel.getStateHistory().observe(viewLifecycleOwner){ state ->
            when(state){
                SearchHistoryState.Empty -> hideHistory()
                is SearchHistoryState.HasValue -> showHistory()
            }
        }
    }

    private fun initListeners() {

        binding.searchErrRefresh.setOnClickListener{
            viewModel.searchDebounce(searchText)
        }

        binding.searchClear.setOnClickListener{
            binding.searchBar.setText("")
            searchResultsAdapterList.clear()
            searchResultsAdapter.notifyDataSetChanged()
            binding.searchResultRecycler.visibility = View.GONE
            if (viewModel.getHistory().isNotEmpty()) {
                refreshHistory()
            }
            else binding.searchHistory.visibility = View.GONE
        }

        binding.searchHistoryClear.setOnClickListener{
            viewModel.clearHistory()
            historyAdapterList.clear()
            searchResultsAdapter.notifyDataSetChanged()
        }

        val searchTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //none
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchInstanceState = s.toString()
                binding.searchClear.visibility = clearButtonVisibility(s)
                binding.searchErrNotFound.visibility = View.GONE
                binding.searchErrNoConnect.visibility = View.GONE
                binding.searchHistory.visibility = if (binding.searchBar.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
                viewModel.searchDebounce(searchText)
            }
        }
        binding.searchBar.addTextChangedListener(searchTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    private fun showResult(){
        binding.searchProgressBar.visibility = View.GONE
        binding.searchResultRecycler.visibility = View.VISIBLE
    }

    private fun showErrorConnection(){
        binding.searchProgressBar.visibility = View.GONE
        binding.searchErrNoConnect.visibility = View.VISIBLE
    }

    private fun showErrorEmptyReponse(){
        binding.searchProgressBar.visibility = View.GONE
        binding.searchErrNotFound.visibility = View.VISIBLE
    }

    private fun showLoading(){
        binding.searchResultRecycler.visibility = View.GONE
        binding.searchErrNotFound.visibility = View.GONE
        binding.searchErrNoConnect.visibility = View.GONE
        binding.searchProgressBar.visibility = View.VISIBLE
    }

    private fun showHistory(){
        binding.searchHistory.visibility = View.VISIBLE
    }

    private fun hideHistory(){
        binding.searchHistory.visibility = View.GONE
    }

    private fun refreshHistory() {
        historyAdapterList.clear()
        historyAdapterList.addAll(viewModel.getHistory())
        historyAdapter.notifyDataSetChanged()
    }
}