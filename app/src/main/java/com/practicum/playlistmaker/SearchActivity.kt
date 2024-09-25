package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var searchTnstanceState = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btn_back = findViewById<ImageView>(R.id.search_back)
        val btn_clear = findViewById<ImageView>(R.id.search_clear)
        val search_bar = findViewById<EditText>(R.id.search_bar)

        btn_back.setOnClickListener{
            finish()
        }

        btn_clear.setOnClickListener{
            search_bar.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(search_bar.windowToken, 0)
        }

        val searchTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //none
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchTnstanceState = s.toString()
                btn_clear.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                //none
            }
        }
        search_bar.addTextChangedListener(searchTextWatcher)

        val trackList: ArrayList<Track> = ArrayList(arrayListOf(
            Track("Smells Like Teen Spirit", "Nirvana", "5:01", getString(R.string.track_image_Nirvana)),
            Track("Billie Jean", "Michael Jackson", "4:35", getString(R.string.track_image_MichaelJackson)),
            Track("Stayin' Alive", "Bee Gees", "4:10", getString(R.string.track_image_BeeGees)),
            Track("Whole Lotta Love", "Led Zeppelin", "5:33", getString(R.string.track_image_LedZeppelin)),
            Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", getString(R.string.track_image_GunsNRoses))
        ))

        val search_result = findViewById<RecyclerView>(R.id.search_result_recycler)
        search_result.adapter = TrackAdapter(trackList)
        search_result.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        outState.putString("SEARCH_BAR", searchTnstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchTnstanceState = savedInstanceState.getString("SEARCH_BAR")
        findViewById<EditText>(R.id.search_bar).setText(searchTnstanceState)
    }
}