package com.practicum.playlistmaker.settings.ui.activity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.ui.viewModel.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel by viewModel<SettingsViewModel>()

        val button_settings_back = findViewById<ImageView>(R.id.button_settings_back)
        button_settings_back.setOnClickListener{
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.settings_btn_share)
        val supportButton = findViewById<FrameLayout>(R.id.settings_btn_support)
        val uaButton = findViewById<FrameLayout>(R.id.settings_btn_ua)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        shareButton.setOnClickListener {
            viewModel.shareApp(getString(R.string.praktikum_url))
        }

        supportButton.setOnClickListener{
            viewModel.writeSupport(
                EmailData(
                    theme = getString(R.string.email_theme),
                    text = getString(R.string.email_text),
                    email = getString(R.string.email)
                )
            )
        }

        uaButton.setOnClickListener{
            viewModel.openUA(getString(R.string.praktikum_offer))
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked -> viewModel.themeSwitch(checked) }
    }
}