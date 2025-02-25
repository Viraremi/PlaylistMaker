package com.practicum.playlistmaker.settings.ui.activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.ui.model.SettingsState
import com.practicum.playlistmaker.settings.ui.viewModel.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    val viewModel by viewModel<SettingsViewModel>()
    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSettingsBack.setOnClickListener{ findNavController().navigateUp() }

        binding.settingsBtnShare.setOnClickListener {
            viewModel.shareApp(getString(R.string.praktikum_url))
        }

        binding.settingsBtnSupport.setOnClickListener{
            viewModel.writeSupport(
                EmailData(
                    theme = getString(R.string.email_theme),
                    text = getString(R.string.email_text),
                    email = getString(R.string.email)
                )
            )
        }

        binding.settingsBtnUa.setOnClickListener{
            viewModel.openUA(getString(R.string.praktikum_offer))
        }

        binding.themeSwitcher.isChecked = when (viewModel.getState().value){
            SettingsState.ON -> true
            else -> false
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked -> viewModel.themeSwitch(checked) }
    }
}