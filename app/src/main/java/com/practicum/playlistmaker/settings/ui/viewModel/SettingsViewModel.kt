package com.practicum.playlistmaker.settings.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.domain.api.InteractorSettings
import com.practicum.playlistmaker.sharing.domain.api.InteractorSharing
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import com.practicum.playlistmaker.util.Creator

class SettingsViewModel(): ViewModel() {

    val settingsInteractor = Creator.provideSettingsInteractor()
    val sharingInteractor = Creator.provideSharingInteractor()

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