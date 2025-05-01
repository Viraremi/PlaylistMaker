package com.practicum.playlistmaker.util

import android.icu.text.SimpleDateFormat
import java.util.Locale

object StringFormatter {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun getValidTimeFormat(time: Long) = dateFormat.format(time)

    fun countString(count: Int): String {

        return when {
            count % 100 in 11..19 -> "$count треков"
            count % 10 == 1 -> "$count трек"
            count % 10 in 2..4 -> "$count трека"
            else -> "$count треков"
        }
    }

    fun minuteString(time: Long): String {

        return when {
            time % 100 in 11..19L -> "$time минут"
            time % 10 == 1L -> "$time минута"
            time % 10 in 2..4L -> "$time минуты"
            else -> "$time минут"
        }
    }
}