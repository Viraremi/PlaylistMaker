package com.practicum.playlistmaker.sharing.domain.interactor

import com.practicum.playlistmaker.sharing.domain.api.InteractorSharing
import com.practicum.playlistmaker.sharing.domain.api.RepositorySharing
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class InteractorSharingImpl (
    private val repository: RepositorySharing,
) : InteractorSharing {
    override fun shareApp(link: String) {
        repository.shareApp(link)
    }

    override fun writeToSupport(msg: EmailData) {
        repository.writeToSupport(msg)
    }

    override fun openUserAgreement(link: String) {
        repository.openUserAgreement(link)
    }
}