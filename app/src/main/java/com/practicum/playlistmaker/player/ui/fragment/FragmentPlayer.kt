package com.practicum.playlistmaker.player.ui.fragment

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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.ui.fragments.playlists.FragmentNewPlaylist
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.RootActivity
import com.practicum.playlistmaker.util.StringFormatter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlayer : Fragment() {

    companion object {
        const val TRACK_ID = "player_intent_key"
        const val FROM_PLAYLIST = "player"
    }

    private var _binding: FragmentPlayerBinding? = null
    private val binding
        get() = _binding!!

    lateinit var track: Track

    val viewModel by viewModel<PlayerViewModel>()
    var from_playlist = false

    private lateinit var addTrackAdapterList: List<Playlist>
    private lateinit var addTrackAdapter: AddTrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        from_playlist = arguments?.getBoolean(FROM_PLAYLIST) ?: false

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        addTrackAdapterList = viewModel.getPlaylists()
        addTrackAdapter = AddTrackAdapter(addTrackAdapterList) { playlist ->
            viewModel.addTrackToPlaylist(playlist, track) { result ->
                if (result) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    msgAddTrackSuccess(playlist.name)
                }
                else {
                    msgAddTrackFail(playlist.name)
                }
            }
        }
        binding.includedBottomSheet.playerPlaylistRecycler.adapter = addTrackAdapter
        binding.includedBottomSheet.playerPlaylistRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.includedBottomSheet.playerBtnCreatePlaylist.setOnClickListener {
            val bundle = Bundle().apply { putBoolean(FragmentNewPlaylist.FROM_PLAYER, true) }
            findNavController().navigate(R.id.action_playerFragment_to_fragmentNewPlaylist, bundle)
        }

        binding.playerBack.setOnClickListener{
            closeFragment()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closeFragment()
        }

        try {
            val trackId = arguments?.getInt(TRACK_ID, -1)
            track = viewModel.getTrackById(trackId!!)
        } catch (e: Exception) {
            val json = arguments?.getString(TRACK_ID)
            track = Gson().fromJson(json, object : TypeToken<Track>() {}.type)
        }

        viewModel.updateFavoriteStatus(track)
        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.placeholder_big)
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.player_art_corner_radius)))
            .into(binding.playerArt)
        binding.playerTrackName .text = track.trackName
        binding.playerTrackArtist .text = track.artistName
        binding.playerTrackTimeValue.text =
            StringFormatter.getValidTimeFormat(track.trackTimeMillis)
        binding.playerTrackAlbumValue.text = track.collectionName
        binding.playerTrackYearValue.text = track.releaseDate.substring(0, 4)
        binding.playerTrackGenreValue.text = track.primaryGenreName
        binding.playerTrackCountryValue.text = track.country
        viewModel.prepare(track.previewUrl)

        binding.playerBtnPlay.setOnClickListener{
            viewModel.playbackControl()
        }

        binding.playerBtnLikeIco.setOnClickListener {
            viewModel.onClickFavorite(track)
        }

        binding.playerBtnAddToListIco.setOnClickListener {
            refreshPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        viewModel.getStatePlayerView().observe(viewLifecycleOwner){ state ->
            binding.playerBtnPlay.isEnabled = state.isPlayButtonEnabled
            setPlayButtonIcon(state.buttonType)
            binding.playerCurrentTime.text = state.progress
        }

        viewModel.getStateFavorite().observe(viewLifecycleOwner){ state ->
            setFavoriteButtonIcon(state)
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })
    }

    override fun onPause() {
        viewModel.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        _binding = null
        viewModel.release()
        super.onDestroyView()
    }

    private fun closeFragment(){
        if (!from_playlist) (activity as RootActivity).animateBottomNavigationView()
        findNavController().popBackStack()
    }

    private fun setPlayButtonIcon(iconType: Boolean) {
        if (iconType) {
            binding.playerBtnPlay.setImageResource(R.drawable.button_play)
        } else {
            binding.playerBtnPlay.setImageResource(R.drawable.button_pause)
        }
    }

    private fun setFavoriteButtonIcon(iconType: Boolean) {
        if (iconType) {
            binding.playerBtnLikeIco.setImageResource(R.drawable.button_like_active)
        }
        else {
            binding.playerBtnLikeIco.setImageResource(R.drawable.button_like)
        }
    }

    fun msgAddTrackSuccess(playlistName: String) {
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.msg_add_track_success, playlistName),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun msgAddTrackFail(playlistName: String) {
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.msg_add_track_fail, playlistName),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun refreshPlaylists() {
        addTrackAdapterList = viewModel.getPlaylists()
        addTrackAdapter.notifyDataSetChanged()
    }
}