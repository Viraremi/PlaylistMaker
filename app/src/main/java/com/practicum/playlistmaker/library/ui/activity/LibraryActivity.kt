package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.ui.model.LibraryViewState
import com.practicum.playlistmaker.library.ui.viewModel.LibraryViewModel

class LibraryActivity : AppCompatActivity() {

    val viewModel by lazy {
        ViewModelProvider(this)[LibraryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_library)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getState().observe(this){state ->
            when(state){
                is LibraryViewState.NOTHING -> {
                    //Скоро здесь будет какой то код
                }
            }
        }
    }
}