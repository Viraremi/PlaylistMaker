package com.practicum.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        val themeInteractor = Creator.provideSettingsInteractor()
        themeInteractor.switchTheme(themeInteractor.getTheme())
    }
}
