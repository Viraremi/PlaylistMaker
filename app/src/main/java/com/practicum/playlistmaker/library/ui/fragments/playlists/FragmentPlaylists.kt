package com.practicum.playlistmaker.library.ui.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import com.practicum.playlistmaker.library.domain.api.InteractorPlaylist
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentPlaylistState
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentPlaylistViewModel
import com.practicum.playlistmaker.util.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylists(): Fragment() {

    companion object {
        fun newInstance() = FragmentPlaylists().apply {
            arguments = Bundle().apply { /* none */ }
        }
    }

    private val adapterList = mutableListOf<Playlist>()
    private val adapter = PlaylistAdapter(adapterList)

    private var _binding: FragmentLibraryPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FragmentPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getState().observe(viewLifecycleOwner){ state ->
            when(state) {
                is FragmentPlaylistState.EMPTY -> showErrorEmpty()
                is FragmentPlaylistState.CONTENT -> showPlaylists(state.data)
            }
        }

        binding.fragmentPlaylistRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.fragmentPlaylistRecycler.adapter = adapter

        binding.btnCreatePlaylist.setOnClickListener {
            (activity as RootActivity).animateBottomNavigationView()
            findNavController().navigate(R.id.action_libraryFragment_to_fragmentNewPlaylist)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showErrorEmpty() {
        binding.fragmentPlaylistErrEmpty.isVisible = true
        binding.fragmentPlaylistRecycler.isVisible = false
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        binding.fragmentPlaylistErrEmpty.isVisible = false
        binding.fragmentPlaylistRecycler.isVisible = true

        adapterList.clear()
        adapterList.addAll(playlists)
        adapter.notifyDataSetChanged()
    }
}