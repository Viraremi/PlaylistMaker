package com.practicum.playlistmaker.library.ui.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
                .setTitle(requireContext().getString(R.string.delete_alert_title))
                .setMessage(requireContext().getString(R.string.delete_alert_message))
                .setNegativeButton(requireContext().getString(R.string.delete_alert_negative)) { _, _ ->  /* none */ }
                .setPositiveButton(requireContext().getString(R.string.delete_alert_positive)) { _, _ ->
                    viewModel.deleteTrackFromPlaylist(currentPlaylists, track)
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

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.showPlaylistBottomSheetMenu)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        binding.showPlaylistBtnMore.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.showPlaylistBtnShare.setOnClickListener {
            sharePlaylist()
        }

        binding.includedBottomSheetMenu.bottomSheetMenuBtnShare.setOnClickListener {
            if (!sharePlaylist()) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        binding.includedBottomSheetMenu.bottomSheetMenuBtnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(requireContext().getString(R.string.delete_playlist))
                .setMessage(requireContext().getString(R.string.delete_playlist_alert_message, currentPlaylists.name))
                .setNegativeButton(requireContext().getString(R.string.delete_alert_negative)) { _, _ ->  /* none */ }
                .setPositiveButton(requireContext().getString(R.string.delete_alert_positive)) { _, _ ->
                    viewModel.deletePlaylist(currentPlaylists)
                    closeFragment()
                }
                .show()
        }

        binding.includedBottomSheetMenu.bottomSheetMenuBtnEdit.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentShowPlaylist_to_fragmentNewPlaylist,
                Bundle().apply {
                    putBoolean(FragmentNewPlaylist.FROM_SECONDARY, true)
                    putString(FragmentNewPlaylist.PLAYLIST, Gson().toJson(currentPlaylists))
                }
            )
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.showPlaylistOverlay.isVisible = false
                    }
                    else -> {
                        binding.showPlaylistOverlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })

        currentPlaylists = viewModel.playlistFromJson(arguments?.getString(PLAYLIST_JSON)!!)

        binding.includedBottomSheetTracks.bottomSheetTracksListRecycler.adapter = adapter
        binding.includedBottomSheetTracks.bottomSheetTracksListRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FragmentShowPlaylistState.CONTENT -> {
                    currentPlaylists = state.playlist
                    Glide.with(this)
                        .load(state.playlist.imgPath)
                        .placeholder(R.drawable.placeholder_big)
                        .into(binding.showPlaylistImg)
                    binding.showPlaylistName.text = state.playlist.name
                    binding.showPlaylistDescription.text = state.playlist.description
                    binding.showPlaylistTime.text = state.timeString
                    binding.showPlaylistCount.text = state.countString

                    refreshRecycler(state.tracks)
                    viewModel.generateMsg(state.playlist, state.tracks, requireContext())

                    when (state.tracks.isEmpty()) {
                        true -> showErrEmpty()
                        false -> showTracks()
                    }

                    binding.includedBottomSheetMenu.includedPlaylistItem.playerPlaylistName.text =
                        state.playlist.name
                    binding.includedBottomSheetMenu.includedPlaylistItem.playerPlaylistCount.text =
                        state.countString
                    Glide.with(this)
                        .load(state.playlist.imgPath)
                        .placeholder(R.drawable.placeholder_medium)
                        .into(binding.includedBottomSheetMenu.includedPlaylistItem.playerPlaylistImg)
                }
            }
        }
    }

    override fun onStart() {
        viewModel.loadContent(currentPlaylists)
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    fun sharePlaylist(): Boolean {
        if (currentPlaylists.tracksCount == 0) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.no_tracks_for_share),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        else {
            viewModel.sharePlaylist()
            return true
        }
    }

    fun showErrEmpty() {
        binding.includedBottomSheetTracks.emptyErr.isVisible = true
        binding.includedBottomSheetTracks.bottomSheetTracksListRecycler.isVisible = false
    }

    fun showTracks() {
        binding.includedBottomSheetTracks.emptyErr.isVisible = false
        binding.includedBottomSheetTracks.bottomSheetTracksListRecycler.isVisible = true
    }
}