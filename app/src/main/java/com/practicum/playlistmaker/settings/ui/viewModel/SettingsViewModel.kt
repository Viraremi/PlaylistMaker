package com.practicum.playlistmaker.settings.ui.viewModel

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.api.InteractorSettings
import com.practicum.playlistmaker.sharing.domain.api.InteractorSharing
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SettingsViewModel(
    val settingsInteractor: InteractorSettings,
    val sharingInteractor:InteractorSharing
): ViewModel() {

    fun themeSwitch(flag: Boolean){
        settingsInteractor.switchTheme(flag)
    }

    fun shareApp(link: String){
        sharingInteractor.shareApp(link)
    }

    fun writeSupport(email: EmailData){
        sharingInteractor.writeToSupport(email)
    }

    fun openUA(link: String){
        sharingInteractor.openUserAgreement(link)
    }
}