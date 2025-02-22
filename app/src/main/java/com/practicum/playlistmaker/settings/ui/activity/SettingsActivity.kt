package com.practicum.playlistmaker.settings.ui.activity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.ui.model.SettingsState
import com.practicum.playlistmaker.settings.ui.viewModel.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    val viewModel by viewModel<SettingsViewModel>()
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getState().observe(this){ state ->
            when (state) {
                SettingsState.OFF -> binding.themeSwitcher.isChecked = false
                SettingsState.ON -> binding.themeSwitcher.isChecked = true
            }
        }

        binding.buttonSettingsBack.setOnClickListener{
            finish()
        }

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

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked -> viewModel.themeSwitch(checked) }
    }
}