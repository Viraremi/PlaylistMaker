package com.practicum.playlistmaker.library.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibraryBinding
import com.practicum.playlistmaker.library.ui.model.LibraryViewState
import com.practicum.playlistmaker.library.ui.viewModel.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.contracts.contract

class LibraryActivity : AppCompatActivity() {

    companion object {

        fun show(context: Context){
            val intent = Intent(context, LibraryActivity::class.java)
            context.startActivity(intent)
        }
    }

    val viewModel by viewModel<LibraryViewModel>()

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_library)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.libraryBtnBack.setOnClickListener { finish() }

        binding.libraryViewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(
            binding.libraryTabLayout, binding.libraryViewPager
        ) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.library_tab_favorite)
                1 -> tab.text = getString(R.string.library_tab_playlist)
            }
        }
        tabMediator.attach()

        viewModel.getState().observe(this){state ->
            when(state){
                is LibraryViewState.NOTHING -> {
                    //Скоро здесь будет какой то код
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}