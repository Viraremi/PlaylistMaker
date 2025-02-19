package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface RepositorySharing {
    fun shareApp (link: String)
    fun writeToSupport (msg: EmailData)
    fun openUserAgreement (link: String)
}