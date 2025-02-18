package com.practicum.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.settings.ui.activity.SettingsActivity
import com.practicum.playlistmaker.library.ui.activity.LibraryActivity
import com.practicum.playlistmaker.main.ui.model.MainViewState
import com.practicum.playlistmaker.main.ui.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val viewPadding = resources.getDimensionPixelSize(R.dimen.padding)
            v.setPadding(
                systemBars.left + viewPadding,
                systemBars.top + viewPadding,
                systemBars.right + viewPadding,
                systemBars.bottom + viewPadding)
            insets
        }

        val button_search = findViewById<Button>(R.id.button_search)
        val button_library = findViewById<Button>(R.id.button_library)
        val button_settings = findViewById<Button>(R.id.button_settings)

        viewModel.getState().observe(this){ state ->
            when(state){
                MainViewState.LIBRARY -> {
                    val libraryIntent = Intent(this@MainActivity, LibraryActivity::class.java)
                    startActivity(libraryIntent)
                }
                MainViewState.SEARCH -> {
                    val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                    startActivity(searchIntent)
                }
                MainViewState.SETTINGS -> {
                    val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                    startActivity(settingsIntent)
                }
            }
        }

        button_search.setOnClickListener {
            viewModel.showSearch()
        }

        button_library.setOnClickListener {
            viewModel.showLibrary()
        }

        button_settings.setOnClickListener {
            viewModel.showSettings()
        }
    }
}