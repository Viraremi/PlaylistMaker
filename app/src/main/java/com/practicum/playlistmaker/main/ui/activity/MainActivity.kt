package com.practicum.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.settings.ui.activity.SettingsActivity
import com.practicum.playlistmaker.library.ui.activity.LibraryActivity
import com.practicum.playlistmaker.main.ui.model.MainViewState
import com.practicum.playlistmaker.main.ui.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

        binding.buttonSearch.setOnClickListener {
            viewModel.showSearch()
        }

        binding.buttonLibrary.setOnClickListener {
            viewModel.showLibrary()
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.showSettings()
        }
    }
}