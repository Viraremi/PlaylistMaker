package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private var searchTnstanceState = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btn_clear = findViewById<ImageView>(R.id.search_clear)
        val search_bar = findViewById<EditText>(R.id.search_bar)

        btn_clear.setOnClickListener{
            search_bar.setText("")
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