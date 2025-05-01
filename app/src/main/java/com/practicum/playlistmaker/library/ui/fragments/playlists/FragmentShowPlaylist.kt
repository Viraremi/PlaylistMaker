package com.practicum.playlistmaker.library.ui.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentShowPlaylistBinding
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentShowPlaylistViewModel
import com.practicum.playlistmaker.search.ui.fragments.TrackAdapter
import com.practicum.playlistmaker.util.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentShowPlaylist : Fragment() {

    companion object {
        const val PLAYLIST_JSON = "playlist_id"
    }

    val viewModel by viewModel<FragmentShowPlaylistViewModel>()

    lateinit var currentPlaylists: Playlist

    private var _binding: FragmentShowPlaylistBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentPlaylists = viewModel.playlistFromJson(arguments?.getString(PLAYLIST_JSON)!!)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            (activity as RootActivity).animateBottomNavigationView()
            findNavController().popBackStack()
        }

        binding.testRecycler.adapter = TrackAdapter(viewModel.getTracks(currentPlaylists)) {/**/}
        binding.testRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}