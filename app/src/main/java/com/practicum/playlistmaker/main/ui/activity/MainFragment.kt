package com.practicum.playlistmaker.main.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.main.ui.model.MainViewState
import com.practicum.playlistmaker.main.ui.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getState().observe(viewLifecycleOwner){ state ->
            when(state){
                MainViewState.LIBRARY -> {
                    findNavController().navigate(R.id.action_mainFragment_to_libraryFragment)
                }
                MainViewState.SEARCH -> {
                    findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
                }
                MainViewState.SETTINGS -> {
                    findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                }
            }
        }

        binding.buttonSearch.setOnClickListener {
            viewModel.showSearch()
        }

        binding.buttonLibrary.setOnClickListener {
            viewModel.showLibrary()
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.showSettings()
        }
    }
}