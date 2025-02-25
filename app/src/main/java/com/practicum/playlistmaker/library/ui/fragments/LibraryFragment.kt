package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryBinding
import com.practicum.playlistmaker.library.ui.model.LibraryViewState
import com.practicum.playlistmaker.library.ui.viewModel.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment() {

    val viewModel by viewModel<LibraryViewModel>()

    private lateinit var tabMediator: TabLayoutMediator

    private var _binding: FragmentLibraryBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.libraryViewPager.adapter = LibraryViewPagerAdapter(
            childFragmentManager, //Очень важный момент для будущей работы с вложенными фрагментами!!!
            lifecycle
        )

        tabMediator = TabLayoutMediator(
            binding.libraryTabLayout, binding.libraryViewPager
        ) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.library_tab_favorite)
                1 -> tab.text = getString(R.string.library_tab_playlist)
            }
        }
        tabMediator.attach()

        viewModel.getState().observe(viewLifecycleOwner){state ->
            when(state){
                is LibraryViewState.NOTHING -> {
                    //Скоро здесь будет какой то код
                }
            }
        }
    }

    override fun onDestroyView() {
        tabMediator.detach()
        _binding = null
        super.onDestroyView()
    }
}