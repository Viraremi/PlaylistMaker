package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentLibraryFavoriteBinding
import com.practicum.playlistmaker.library.ui.model.FragmentFavoriteState
import com.practicum.playlistmaker.library.ui.viewModel.FragmentFavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavorite : Fragment() {

    companion object {
        fun newInstance() = FragmentFavorite().apply {
            arguments = Bundle().apply { /* none */ }
        }
    }

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
        viewModel.getState().observe(viewLifecycleOwner){ state ->
            when(state) {
                FragmentFavoriteState.Empty -> showErrorEmpty()
                is FragmentFavoriteState.Content -> TODO()
            }
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