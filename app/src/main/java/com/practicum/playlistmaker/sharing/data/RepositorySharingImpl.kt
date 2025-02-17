package com.practicum.playlistmaker.sharing.data

import android.content.Context
import com.practicum.playlistmaker.sharing.domain.api.RepositorySharing
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class RepositorySharingImpl(
    private val context: Context
) : RepositorySharing {

    override fun shareApp(link: String) {
        TODO("Not yet implemented")
    }

    override fun writeToSupport(msg: EmailData) {
        TODO("Not yet implemented")
    }

    override fun openUserAgreement(link: String) {
        TODO("Not yet implemented")
    }
}