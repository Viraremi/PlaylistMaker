package com.practicum.playlistmaker.settings.domain.api

interface RepositorySettings {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getTheme(): Boolean
}