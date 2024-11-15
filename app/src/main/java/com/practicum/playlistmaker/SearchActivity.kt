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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
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
    private lateinit var recyclerAdapter: TrackAdapter
    private var searchInstanceState = ""
    private var searchText = ""
    private var adapterList = mutableListOf<Track>()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val tracksAPI = retrofit.create(ITunesAPI::class.java)

    override fun onStop() {
        super.onStop()
        searchHistory.save()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btn_back = findViewById<ImageView>(R.id.search_back)
        val btn_clear = findViewById<ImageView>(R.id.search_clear)
        val search_bar = findViewById<EditText>(R.id.search_bar)
        val err_btn_refrech = findViewById<Button>(R.id.search_err_refresh)
        val err_found = findViewById<LinearLayout>(R.id.search_err_not_found)
        val err_connect = findViewById<LinearLayout>(R.id.search_err_no_connect)
        val history_header = findViewById<TextView>(R.id.search_history_header)
        val btn_history_clear = findViewById<Button>(R.id.search_history_clear)

        searchHistory = SearchHistory(getSharedPreferences(HISTORY, MODE_PRIVATE))
        adapterList.addAll(searchHistory.history)
        recyclerAdapter = TrackAdapter(adapterList) { track -> searchHistory.add(track) }
        if (adapterList.isNotEmpty()) setHistoryVisibility(true, history_header, btn_history_clear)

        btn_back.setOnClickListener{
            finish()
        }

        btn_clear.setOnClickListener{
            search_bar.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(search_bar.windowToken, 0)
            adapterList.clear()
            if (searchHistory.history.isNotEmpty()){
                setHistoryVisibility(true, history_header, btn_history_clear)
                err_found.visibility = View.GONE
                err_connect.visibility = View.GONE
                adapterList.addAll(searchHistory.history)
            }
            recyclerAdapter.notifyDataSetChanged()
        }

        err_btn_refrech.setOnClickListener{
            searchRequest(searchText,err_found, err_connect)
        }

        btn_history_clear.setOnClickListener{
            searchHistory.clear()
            adapterList.clear()
            recyclerAdapter.notifyDataSetChanged()
            setHistoryVisibility(false, history_header, btn_history_clear)
        }

        val searchTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //none
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchInstanceState = s.toString()
                btn_clear.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }
        search_bar.addTextChangedListener(searchTextWatcher)
        search_bar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRequest(searchText, err_found, err_connect)
                setHistoryVisibility(false, history_header, btn_history_clear)
                true
            }
            false
        }

        val searchRecycleView = findViewById<RecyclerView>(R.id.search_result_recycler)
        searchRecycleView.adapter = recyclerAdapter
        searchRecycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        val searchTnstanceState = savedInstanceState.getString("SEARCH_BAR")
        findViewById<EditText>(R.id.search_bar).setText(searchTnstanceState)
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
                        adapterList.clear()
                        err_connect.visibility = View.GONE
                        err_found.visibility = View.VISIBLE
                    }
                    else {
                        err_found.visibility = View.GONE
                        err_connect.visibility = View.GONE
                        adapterList.clear()
                        adapterList.addAll(result)
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
                else {
                    adapterList.clear()
                    err_found.visibility = View.GONE
                    err_connect.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseTracks>, t: Throwable) {
                adapterList.clear()
                err_found.visibility = View.GONE
                err_connect.visibility = View.VISIBLE
            }
        })
    }

    private fun setHistoryVisibility(visibility: Boolean, header: TextView, btn: Button){
        if (visibility){
            header.visibility = View.VISIBLE
            btn.visibility = View.VISIBLE
        }
        else {
            header.visibility = View.GONE
            btn.visibility = View.GONE
        }
    }
}