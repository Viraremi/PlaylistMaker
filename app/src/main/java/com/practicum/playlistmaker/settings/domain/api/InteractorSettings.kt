package com.practicum.playlistmaker.settings.domain.api

interface InteractorSettings {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getTheme(): Boolean
}