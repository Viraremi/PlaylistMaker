package com.practicum.playlistmaker.library.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryFavoriteBinding
import com.practicum.playlistmaker.library.ui.model.FragmentFavoriteState
import com.practicum.playlistmaker.library.ui.viewModel.favorite.FragmentFavoriteViewModel
import com.practicum.playlistmaker.player.ui.fragment.FragmentPlayer
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.fragments.TrackAdapter
import com.practicum.playlistmaker.util.RootActivity
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavorite : Fragment() {

    companion object {
        fun newInstance() = FragmentFavorite()

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
            (activity as RootActivity).animateBottomNavigationView()
            val bundle = Bundle().apply { putString(FragmentPlayer.TRACK_ID, Gson().toJson(track)) }
            findNavController().navigate(R.id.action_libraryFragment_to_playerFragment, bundle)
        }

        favoriteAdapter = TrackAdapter(favoriteList, longClick = { /* none */ },
            click = { track ->
                onClickDebounce(track)
            }
        )
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
        binding.favoriteRecycler.isVisible = false
        binding.favoriteErrEmpty.isVisible = true
    }

    private fun showContent(tracks: List<Track>) {
        binding.favoriteRecycler.isVisible = true
        binding.favoriteErrEmpty.isVisible = false

        favoriteList.clear()
        favoriteList.addAll(tracks)
        favoriteAdapter.notifyDataSetChanged()
    }
}