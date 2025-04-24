package com.practicum.playlistmaker.player.ui.model

sealed class PlayerViewState(val isPlayButtonEnabled: Boolean, val buttonType: Boolean, val progress: String) {
    class Default : PlayerViewState(false, true, "00:00")
    class Prepared : PlayerViewState(true, true, "00:00")
    class Playing(progress: String) : PlayerViewState(true, false, progress)
    class Paused(progress: String) : PlayerViewState(true, true, progress)
}