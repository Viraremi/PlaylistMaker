package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object{
        const val BASE_URL = "https://itunes.apple.com"
        const val HISTORY = "search_history"
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

        val gson = Gson()
        val btn_back = findViewById<ImageView>(R.id.search_back)
        val btn_clear = findViewById<ImageView>(R.id.search_clear)
        val search_bar = findViewById<EditText>(R.id.search_bar)
        val searchRecycleView = findViewById<RecyclerView>(R.id.search_result_recycler)
        val err_btn_refrech = findViewById<Button>(R.id.search_err_refresh)
        val err_found = findViewById<LinearLayout>(R.id.search_err_not_found)
        val err_connect = findViewById<LinearLayout>(R.id.search_err_no_connect)
        val historyLayout = findViewById<ScrollView>(R.id.search_history)
        val btn_history_clear = findViewById<Button>(R.id.search_history_clear)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.search_history_recycler)

        searchHistory = SearchHistory(getSharedPreferences(HISTORY, MODE_PRIVATE), gson)
        historyAdapterList.addAll(searchHistory.getHistory())
        if (searchHistory.getHistory().isEmpty()) historyLayout.visibility = View.GONE

        btn_back.setOnClickListener{
            finish()
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

        err_btn_refrech.setOnClickListener{
            searchRequest(searchText,err_found, err_connect)
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
                historyLayout.visibility = if (search_bar.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }
        search_bar.addTextChangedListener(searchTextWatcher)
        search_bar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                historyLayout.visibility = View.GONE
                searchRecycleView.visibility = View.VISIBLE
                searchRequest(searchText, err_found, err_connect)
                true
            }
            false
        }

        /*
        Не совсем понял зачем нам тут отслеживать фокус, как это нам советует
        подсказка в ТЗ на платформе. Для нашей задачи достаточно условия в
        TextWatcher.onTextChanged, а при добавлении/удалении этого слушателя
        поведение приложения не меняется

        search_bar.setOnFocusChangeListener { _, hasFocus ->
            historyLayout.visibility = if (hasFocus && search_bar.text.isEmpty()) View.VISIBLE else View.GONE
        }
        */

        searchResultsAdapter = TrackAdapter(searchResultsAdapterList) { track -> searchHistory.add(track) }
        searchRecycleView.adapter = searchResultsAdapter
        searchRecycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        historyAdapter = TrackAdapter(historyAdapterList) {}
        historyAdapter.notifyDataSetChanged()
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

    private fun searchRequest(text: String, err_found: LinearLayout, err_connect: LinearLayout){
        tracksAPI.getTrack(text).enqueue(object : Callback<ResponseTracks>{
            override fun onResponse(
                call: Call<ResponseTracks>,
                response: Response<ResponseTracks>
            ) {
                val result = response.body()?.foundTracks
                if (response.isSuccessful && result != null){
                    if (result.isEmpty()){
                        searchResultsAdapterList.clear()
                        err_connect.visibility = View.GONE
                        err_found.visibility = View.VISIBLE
                    }
                    else {
                        err_found.visibility = View.GONE
                        err_connect.visibility = View.GONE
                        searchResultsAdapterList.clear()
                        searchResultsAdapterList.addAll(result)
                        searchResultsAdapter.notifyDataSetChanged()
                    }
                }
                else {
                    searchResultsAdapterList.clear()
                    err_found.visibility = View.GONE
                    err_connect.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseTracks>, t: Throwable) {
                searchResultsAdapterList.clear()
                err_found.visibility = View.GONE
                err_connect.visibility = View.VISIBLE
            }
        })
    }
}