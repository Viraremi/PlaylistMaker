package com.practicum.playlistmaker.main.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.search.ui.activity.SearchFragment
import com.practicum.playlistmaker.library.ui.activity.LibraryActivity
import com.practicum.playlistmaker.main.ui.model.MainViewState
import com.practicum.playlistmaker.main.ui.viewModel.MainViewModel
import com.practicum.playlistmaker.settings.ui.activity.SettingsFragment
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

    private fun goToFragment(fragment: Fragment){
        parentFragmentManager.commit {
            replace(R.id.root_fragment_container, fragment)
            addToBackStack(null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getState().observe(viewLifecycleOwner){ state ->
            when(state){
                MainViewState.LIBRARY -> {
                    LibraryActivity.show(requireContext())
                }
                MainViewState.SEARCH -> { goToFragment(SearchFragment.newInstance()) }
                MainViewState.SETTINGS -> { goToFragment(SettingsFragment.newInstance()) }
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