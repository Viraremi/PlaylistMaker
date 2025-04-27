package com.practicum.playlistmaker.library.ui.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import com.practicum.playlistmaker.library.ui.model.FragmentPlaylistState
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentPlaylistViewModel
import com.practicum.playlistmaker.util.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylists: Fragment() {

    companion object {
        fun newInstance() = FragmentPlaylists().apply {
            arguments = Bundle().apply { /* none */ }
        }
    }

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
                FragmentPlaylistState.EMPTY -> showErrorEmpty()
            }
        }

        binding.btnCreatePlaylist.setOnClickListener {
            (activity as RootActivity).animateBottomNavigationView()
            findNavController().navigate(R.id.action_libraryFragment_to_fragmentNewPlaylist)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showErrorEmpty() {
        /* Пока здесь ничего нет */
    }
}