package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.ui.model.SearchHistoryState
import com.practicum.playlistmaker.search.ui.model.SearchState
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    companion object{
        const val PLAYER_INTENT_KEY = "player_intent_key"
    }

    val viewModel by viewModel<SearchViewModel>()

    lateinit var err_connect: LinearLayout
    lateinit var err_found: LinearLayout
    lateinit var progressBar: ProgressBar
    lateinit var searchRecycleView: RecyclerView
    lateinit var historyLayout: ScrollView

    private lateinit var searchResultsAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private var searchInstanceState = ""
    private var searchText = ""
    private var searchResultsAdapterList = mutableListOf<Track>()
    private var historyAdapterList = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets((WindowInsetsCompat.Type.ime()))
            val paddingBottom = if (ime.bottom > 0) { ime.bottom } else { systemBars.bottom }
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, paddingBottom)
            insets
        }

        val btn_back = findViewById<ImageView>(R.id.search_back)
        val btn_clear = findViewById<ImageView>(R.id.search_clear)
        val search_bar = findViewById<EditText>(R.id.search_bar)
        val err_btn_refrech = findViewById<Button>(R.id.search_err_refresh)
        searchRecycleView = findViewById(R.id.search_result_recycler)
        err_found = findViewById(R.id.search_err_not_found)
        err_connect = findViewById(R.id.search_err_no_connect)
        historyLayout = findViewById(R.id.search_history)
        val btn_history_clear = findViewById<Button>(R.id.search_history_clear)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.search_history_recycler)
        progressBar = findViewById(R.id.search_progress_bar)

        historyAdapterList.addAll(viewModel.getHistory())

        viewModel.getHistory()

        btn_back.setOnClickListener{
            finish()
        }

        err_btn_refrech.setOnClickListener{
            viewModel.searchDebounce(searchText)
        }

        btn_clear.setOnClickListener{
            search_bar.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(search_bar.windowToken, 0)
            searchResultsAdapterList.clear()
            searchResultsAdapter.notifyDataSetChanged()
            searchRecycleView.visibility = View.GONE
            if (viewModel.getHistory().isNotEmpty()) {
                //Вот эти строки
                historyAdapterList.clear()
                historyAdapterList.addAll(viewModel.getHistory())
                historyAdapter.notifyDataSetChanged()
                //При надобности можно будет вынести в отдельную функцию historyRefresh
            }
            else historyLayout.visibility = View.GONE
        }

        btn_history_clear.setOnClickListener{
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
                btn_clear.visibility = clearButtonVisibility(s)
                err_found.visibility = View.GONE
                err_connect.visibility = View.GONE
                historyLayout.visibility = if (search_bar.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
                viewModel.searchDebounce(searchText)
            }
        }
        search_bar.addTextChangedListener(searchTextWatcher)

        searchResultsAdapter = TrackAdapter(searchResultsAdapterList) { track ->
            if (viewModel.clickDebounce()) {
                viewModel.add(track)
                val playerIntent = Intent(this, PlayerActivity::class.java)
                startActivity(playerIntent.putExtra(PLAYER_INTENT_KEY, viewModel.getTrackId(track)))
            }
        }
        searchRecycleView.adapter = searchResultsAdapter
        searchRecycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        historyAdapter = TrackAdapter(historyAdapterList) { track ->
            if (viewModel.clickDebounce()) {
                val playerIntent = Intent(this, PlayerActivity::class.java)
                startActivity(playerIntent.putExtra(PLAYER_INTENT_KEY, viewModel.getTrackId(track)))
            }
        }
        historyAdapter.notifyDataSetChanged()
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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
        progressBar.visibility = View.GONE
        searchRecycleView.visibility = View.VISIBLE
    }

    private fun showErrorConnection(){
        progressBar.visibility = View.GONE
        err_connect.visibility = View.VISIBLE
    }

    private fun showErrorEmptyReponse(){
        progressBar.visibility = View.GONE
        err_found.visibility = View.VISIBLE
    }

    private fun showLoading(){
        searchRecycleView.visibility = View.GONE
        err_found.visibility = View.GONE
        err_connect.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showHistory(){
        historyLayout.visibility = View.VISIBLE
    }

    private fun hideHistory(){
        historyLayout.visibility = View.GONE
    }
}