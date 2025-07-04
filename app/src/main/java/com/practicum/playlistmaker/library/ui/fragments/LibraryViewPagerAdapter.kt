package com.practicum.playlistmaker.library.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.library.ui.fragments.favorite.FragmentFavorite
import com.practicum.playlistmaker.library.ui.fragments.playlists.FragmentPlaylists

class LibraryViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            FragmentFavorite.newInstance()
        } else {
            FragmentPlaylists.newInstance()
        }
    }
}