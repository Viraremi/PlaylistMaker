package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.ui.UI_THEME
import com.practicum.playlistmaker.ui.UI_THEME_KEY

class App : Application() {

    private var darkTheme = true

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)

        val sharedPref = getSharedPreferences(UI_THEME, MODE_PRIVATE)
        darkTheme = sharedPref.getBoolean(UI_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
