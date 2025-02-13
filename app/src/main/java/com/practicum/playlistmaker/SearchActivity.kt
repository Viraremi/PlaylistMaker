package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class SearchActivity : AppCompatActivity() {
    companion object{
        const val BASE_URL = "https://itunes.apple.com"
        const val HISTORY = "search_history"
        const val PLAYER_INTENT_KEY = "player_intent_key"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    lateinit var err_connect: LinearLayout
    lateinit var err_found: LinearLayout
    lateinit var progressBar: ProgressBar
    lateinit var searchRecycleView: RecyclerView
    private val searchRunnable = Runnable { searchRequest() }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private lateinit var searchHistory: SearchHistory
    private lateinit var searchResultsAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private var searchInstanceState = ""
    private var searchText = ""
    private var searchResultsAdapterList = mutableListOf<Track>()
    private var historyAdapterList = mutableListOf<Track>()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val tracksAPI = retrofit.create(ITunesAPI::class.java)

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

        val gson = Gson()
        val btn_back = findViewById<ImageView>(R.id.search_back)
        val btn_clear = findViewById<ImageView>(R.id.search_clear)
        val search_bar = findViewById<EditText>(R.id.search_bar)
        val err_btn_refrech = findViewById<Button>(R.id.search_err_refresh)
        searchRecycleView = findViewById(R.id.search_result_recycler)
        err_found = findViewById(R.id.search_err_not_found)
        err_connect = findViewById(R.id.search_err_no_connect)
        val historyLayout = findViewById<ScrollView>(R.id.search_history)
        val btn_history_clear = findViewById<Button>(R.id.search_history_clear)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.search_history_recycler)
        progressBar = findViewById(R.id.search_progress_bar)

        searchHistory = SearchHistory(getSharedPreferences(HISTORY, MODE_PRIVATE), gson)
        historyAdapterList.addAll(searchHistory.getHistory())
        if (searchHistory.getHistory().isEmpty()) historyLayout.visibility = View.GONE

        btn_back.setOnClickListener{
            finish()
        }

        err_btn_refrech.setOnClickListener{
            searchDebounce()
        }

        btn_clear.setOnClickListener{
            search_bar.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(search_bar.windowToken, 0)
            searchResultsAdapterList.clear()
            searchResultsAdapter.notifyDataSetChanged()
            searchRecycleView.visibility = View.GONE
            if (searchHistory.getHistory().isNotEmpty()) {
                //Вот эти строки
                historyAdapterList.clear()
                historyAdapterList.addAll(searchHistory.getHistory())
                historyAdapter.notifyDataSetChanged()
                //При надобности можно будет вынести в отдельную функцию historyRefresh
                historyLayout.visibility = View.VISIBLE
            }
            else historyLayout.visibility = View.GONE
        }

        btn_history_clear.setOnClickListener{
            searchHistory.clear()
            historyAdapterList.clear()
            searchResultsAdapter.notifyDataSetChanged()
            historyLayout.visibility = View.GONE
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
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }
        search_bar.addTextChangedListener(searchTextWatcher)

        searchResultsAdapter = TrackAdapter(searchResultsAdapterList) { track ->
            if (clickDebounce()) {
                searchHistory.add(track)
                val playerIntent = Intent(this, PlayerActivity::class.java)
                startActivity(playerIntent.putExtra(PLAYER_INTENT_KEY, track as Serializable))
            }
        }
        searchRecycleView.adapter = searchResultsAdapter
        searchRecycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        historyAdapter = TrackAdapter(historyAdapterList) { track ->
            if (clickDebounce()) {
                val playerIntent = Intent(this, PlayerActivity::class.java)
                startActivity(playerIntent.putExtra(PLAYER_INTENT_KEY, track as Serializable))
            }
        }
        historyAdapter.notifyDataSetChanged()
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
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

    private fun searchRequest(){
        if (searchText.isNotEmpty()) {
            showLoading()
            tracksAPI.getTrack(searchText).enqueue(object : Callback<ResponseTracks> {
                override fun onResponse(
                    call: Call<ResponseTracks>,
                    response: Response<ResponseTracks>
                ) {
                    val result = response.body()?.foundTracks
                    if (response.isSuccessful && result != null) {
                        if (result.isEmpty()) {
                            showErrorEmptyReponse()
                            searchResultsAdapterList.clear()
                        } else {
                            searchResultsAdapterList.clear()
                            searchResultsAdapterList.addAll(result)
                            searchResultsAdapter.notifyDataSetChanged()
                            searchRecycleView.visibility = View.VISIBLE
                        }
                    } else {
                        searchResultsAdapterList.clear()
                        err_connect.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<ResponseTracks>, t: Throwable) {
                    showErrorConnection()
                    searchResultsAdapterList.clear()
                }
            })
        }
    }

    fun showErrorConnection(){
        progressBar.visibility = View.GONE
        err_connect.visibility = View.VISIBLE
    }

    fun showErrorEmptyReponse(){
        progressBar.visibility = View.GONE
        err_found.visibility = View.VISIBLE
    }

    fun showLoading(){
        searchRecycleView.visibility = View.GONE
        err_found.visibility = View.GONE
        err_connect.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }
}