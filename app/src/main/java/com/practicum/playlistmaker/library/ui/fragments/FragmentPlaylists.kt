package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import com.practicum.playlistmaker.library.ui.model.FragmentPlaylistState
import com.practicum.playlistmaker.library.ui.viewModel.FragmentPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylists: Fragment() {

    companion object {
        fun newInstance() = FragmentPlaylists().apply {
            arguments = Bundle().apply { /* none */ }
        }
    }

    private var _binding: FragmentLibraryPlaylistsBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<FragmentPlaylistViewModel>()

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
    }

    fun showErrorEmpty() {
        /* Пока здесь ничего нет */
    }
}