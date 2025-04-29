package com.practicum.playlistmaker.util

import android.icu.text.SimpleDateFormat
import java.util.Locale

object StringFormatter {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun getValidTimeFormat(time: Long) = dateFormat.format(time)

    fun countString(count: Int): String {
        return when (count) {
            1 -> "$count трек"
            in 2..4 -> "$count трека"
            else -> "$count треков"
        }
    }
}