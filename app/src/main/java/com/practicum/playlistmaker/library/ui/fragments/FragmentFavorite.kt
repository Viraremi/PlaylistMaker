package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryFavoriteBinding
import com.practicum.playlistmaker.library.ui.model.FragmentFavoriteState
import com.practicum.playlistmaker.library.ui.viewModel.FragmentFavoriteViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.fragments.TrackAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavorite : Fragment() {

    companion object {
        fun newInstance() = FragmentFavorite().apply {
            arguments = Bundle().apply { /* none */ }
        }

        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

    private lateinit var onClickDebounce: (Track) -> Unit

    private lateinit var favoriteAdapter: TrackAdapter
    val favoriteList = mutableListOf<Track>()

    private var _binding: FragmentLibraryFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FragmentFavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            val bundle = Bundle().apply { putString(PlayerActivity.TRACK_ID, Gson().toJson(track)) }
            findNavController().navigate(R.id.action_libraryFragment_to_playerActivity, bundle)
        }

        favoriteAdapter = TrackAdapter(favoriteList) { track ->
            onClickDebounce(track)
        }
        binding.favoriteRecycler.adapter = favoriteAdapter
        binding.favoriteRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false)

        viewModel.getState().observe(viewLifecycleOwner){ state ->
            when(state) {
                FragmentFavoriteState.Empty -> showErrorEmpty()
                is FragmentFavoriteState.Content -> showContent(state.data)
            }
        }
    }

    override fun onStart() {
        viewModel.getFavorite()
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showErrorEmpty() {
        binding.favoriteRecycler.visibility = View.GONE
        binding.favoriteErrEmpty.visibility = View.VISIBLE
    }

    private fun showContent(tracks: List<Track>) {
        binding.favoriteRecycler.visibility = View.VISIBLE
        binding.favoriteErrEmpty.visibility = View.GONE

        favoriteList.clear()
        favoriteList.addAll(tracks)
        favoriteAdapter.notifyDataSetChanged()
    }
}