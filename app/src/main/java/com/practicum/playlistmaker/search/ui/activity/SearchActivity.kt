package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.library.ui.activity.LibraryActivity
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.ui.model.SearchHistoryState
import com.practicum.playlistmaker.search.ui.model.SearchState
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    companion object {
        const val PLAYER_INTENT_KEY = "player_intent_key"

        fun show(context: Context){
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivitySearchBinding
    val viewModel by viewModel<SearchViewModel>()

    private lateinit var searchResultsAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private var searchInstanceState = ""
    private var searchText = ""
    private var searchResultsAdapterList = mutableListOf<Track>()
    private var historyAdapterList = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets((WindowInsetsCompat.Type.ime()))
            val paddingBottom = if (ime.bottom > 0) { ime.bottom } else { systemBars.bottom }
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, paddingBottom)
            insets
        }

        historyAdapterList.addAll(viewModel.getHistory())

        initListeners()

        searchResultsAdapter = TrackAdapter(searchResultsAdapterList) { track ->
            if (viewModel.clickDebounce()) {
                viewModel.add(track)
                val playerIntent = Intent(this, PlayerActivity::class.java)
                startActivity(playerIntent.putExtra(PLAYER_INTENT_KEY, viewModel.getTrackId(track)))
            }
        }
        binding.searchResultRecycler.adapter = searchResultsAdapter
        binding.searchResultRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        historyAdapter = TrackAdapter(historyAdapterList) { track ->
            if (viewModel.clickDebounce()) {
                val playerIntent = Intent(this, PlayerActivity::class.java)
                startActivity(playerIntent.putExtra(PLAYER_INTENT_KEY, viewModel.getTrackId(track)))
            }
        }
        historyAdapter.notifyDataSetChanged()
        binding.searchHistoryRecycler.adapter = historyAdapter
        binding.searchHistoryRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel.getState().observe(this){ state ->
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

        viewModel.getStateHistory().observe(this){ state ->
            when(state){
                SearchHistoryState.Empty -> hideHistory()
                is SearchHistoryState.HasValue -> showHistory()
            }
        }
    }

    private fun initListeners() {
        binding.searchBack.setOnClickListener{
            finish()
        }

        binding.searchErrRefresh.setOnClickListener{
            viewModel.searchDebounce(searchText)
        }

        binding.searchClear.setOnClickListener{
            binding.searchBar.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
            searchResultsAdapterList.clear()
            searchResultsAdapter.notifyDataSetChanged()
            binding.searchResultRecycler.visibility = View.GONE
            if (viewModel.getHistory().isNotEmpty()) {
                //Вот эти строки
                historyAdapterList.clear()
                historyAdapterList.addAll(viewModel.getHistory())
                historyAdapter.notifyDataSetChanged()
                //При надобности можно будет вынести в отдельную функцию historyRefresh
            }
            else binding.searchHistory.visibility = View.GONE
        }

        binding.searchHistoryClear.setOnClickListener{
            viewModel.clear()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_BAR", searchInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchInstanceState = savedInstanceState.getString("SEARCH_BAR")
        findViewById<EditText>(R.id.search_bar).setText(searchInstanceState)
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
}