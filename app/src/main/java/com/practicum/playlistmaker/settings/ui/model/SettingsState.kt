package com.practicum.playlistmaker.settings.ui.model

sealed interface SettingsState {
    data object ON: SettingsState
    data object OFF: SettingsState
}