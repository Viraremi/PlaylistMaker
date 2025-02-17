package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.sharing.domain.api.RepositorySharing
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class RepositorySharingImpl(
    private val context: Context
) : RepositorySharing {

    override fun shareApp(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun writeToSupport(msg: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.setData(Uri.parse("mailto:"))
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(msg.email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, msg.theme)
        supportIntent.putExtra(Intent.EXTRA_TEXT, msg.text)
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }

    override fun openUserAgreement(link: String) {
        val uaIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        uaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity((uaIntent))
    }
}