package com.practicum.playlistmaker.settings.domain.Interactor

import com.practicum.playlistmaker.settings.domain.api.InteractorSettings
import com.practicum.playlistmaker.settings.domain.api.RepositorySettings

class InteractorSettingsImpl(
    private val repository: RepositorySettings
) : InteractorSettings {

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }

    override fun getTheme(): Boolean {
        return repository.getTheme()
    }
}