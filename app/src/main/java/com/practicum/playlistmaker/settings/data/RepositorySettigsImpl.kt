package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.api.RepositorySettings

class RepositorySettigsImpl(
    val sharedPref: SharedPreferences
) : RepositorySettings {

    companion object{
        const val UI_THEME_KEY = "ui_theme_key"
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        sharedPref.edit()
            .putBoolean(UI_THEME_KEY, darkThemeEnabled)
            .apply()
    }

    override fun getTheme(): Boolean {
        return sharedPref.getBoolean(UI_THEME_KEY, false)
    }
}