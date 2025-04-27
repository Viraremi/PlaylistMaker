package com.practicum.playlistmaker.library.ui.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.practicum.playlistmaker.library.ui.model.FragmentNewPlaylistState
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentNewPlaylistViewModel
import com.practicum.playlistmaker.util.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentNewPlaylist: Fragment() {
    companion object {
        fun newInstance() = FragmentPlaylists().apply {
            arguments = Bundle().apply { /* none */ }
        }
    }

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FragmentNewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getState().observe(viewLifecycleOwner){ state ->
            when (state) {
                FragmentNewPlaylistState.NOTHING -> {

                }
            }
        }

        binding.addPlaylistBtnBack.setOnClickListener {
            (activity as RootActivity).animateBottomNavigationView()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}