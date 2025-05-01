package com.practicum.playlistmaker.library.ui.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentShowPlaylistBinding
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.model.FragmentShowPlaylistState
import com.practicum.playlistmaker.library.ui.viewModel.playlists.FragmentShowPlaylistViewModel
import com.practicum.playlistmaker.player.ui.fragment.FragmentPlayer
import com.practicum.playlistmaker.search.domain.model.Track
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

    private val adapterList = mutableListOf<Track>()
    private val adapter = TrackAdapter(adapterList,
        click = { track ->
            findNavController().navigate(
                R.id.action_fragmentShowPlaylist_to_playerFragment,
                Bundle().apply {
                    putBoolean(FragmentPlayer.FROM_PLAYLIST, true)
                    putString(FragmentPlayer.TRACK_ID, Gson().toJson(track))
                }
            )
        },
        longClick = { track ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Удалить трек")
                .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
                .setNegativeButton("Отмена") { _, _ ->  /* none */ }
                .setPositiveButton("Удалить") { _, _ ->
                    viewModel.deleteTrackFromPlaylist(currentPlaylists, track)
                    viewModel.loadContent(currentPlaylists)
                }
                .show()
        }
    )

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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closeFragment()
        }

        binding.showPlaylistBtnBack.setOnClickListener {
            closeFragment()
        }

        currentPlaylists = viewModel.playlistFromJson(arguments?.getString(PLAYLIST_JSON)!!)
        viewModel.loadContent(currentPlaylists)

        binding.includedBottomSheet.bottomSheetTracksListRecycler.adapter = adapter
        binding.includedBottomSheet.bottomSheetTracksListRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FragmentShowPlaylistState.CONTENT -> {
                    Glide.with(this)
                        .load(state.playlist.imgPath)
                        .placeholder(R.drawable.placeholder_big)
                        .into(binding.showPlaylistImg)
                    binding.showPlaylistName.text = state.playlist.name
                    binding.showPlaylistDescription.text = state.playlist.description
                    binding.showPlaylistTime.text = state.timeString
                    binding.showPlaylistCount.text = state.countString
                    refreshRecycler(state.tracks)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadContent(currentPlaylists)
    }

    private fun closeFragment(){
        (activity as RootActivity).animateBottomNavigationView()
        findNavController().popBackStack()
    }

    private fun refreshRecycler(tracks: List<Track>) {
        adapterList.clear()
        adapterList.addAll(tracks)
        adapter.notifyDataSetChanged()
    }
}