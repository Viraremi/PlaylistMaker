package com.practicum.playlistmaker.util

import android.icu.text.SimpleDateFormat
import java.util.Locale

object TimeFormatter {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun getValidTimeFormat(time: Long) = dateFormat.format(time)
}