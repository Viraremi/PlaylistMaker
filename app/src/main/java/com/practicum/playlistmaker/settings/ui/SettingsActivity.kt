package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.Creator

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

        val button_settings_back = findViewById<ImageView>(R.id.button_settings_back)
        button_settings_back.setOnClickListener{
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.settings_btn_share)
        val supportButton = findViewById<FrameLayout>(R.id.settings_btn_support)
        val uaButton = findViewById<FrameLayout>(R.id.settings_btn_ua)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.praktikum_url))
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.setData(Uri.parse("mailto:"))
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            startActivity(supportIntent)
        }

        uaButton.setOnClickListener{
            val uaIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.praktikum_offer)))
            startActivity((uaIntent))
        }

        val themeInteractor = Creator.provideSettingsInteractor()
        themeSwitcher.isChecked = themeInteractor.getTheme()
        themeSwitcher.setOnCheckedChangeListener { _, checked -> themeInteractor.switchTheme(checked) }
    }
}