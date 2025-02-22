package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.ui.model.LibraryViewState
import com.practicum.playlistmaker.library.ui.viewModel.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryActivity : AppCompatActivity() {

    val viewModel by viewModel<LibraryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_library)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.library_btn_back).setOnClickListener {
            finish()
        }

        findViewById<ViewPager2>(R.id.library_viewPager).adapter = LibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        viewModel.getState().observe(this){state ->
            when(state){
                is LibraryViewState.NOTHING -> {
                    //Скоро здесь будет какой то код
                }
            }
        }
    }
}