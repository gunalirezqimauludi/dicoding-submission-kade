package com.gunalirezqi.footballclub.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTime {
    @SuppressLint("SimpleDateFormat")
    private fun formatDate(date: String, format: String): String {
        var result = ""
        val old = SimpleDateFormat("yyyy-MM-dd")

        try {
            val oldDate = old.parse(date)
            val newFormat = SimpleDateFormat(format, Locale.getDefault())

            result = newFormat.format(oldDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return result
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatTime(time: String, format: String): String {
        var result = ""
        val old = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        try {
            val oldTime = old.parse(time)
            val newFormat = SimpleDateFormat(format)

            result = newFormat.format(oldTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return result
    }

    fun getDate(date: String): String {
        return formatDate(date, "EEE, dd MMM yyyy")
    }

    fun getTime(time: String): String {
        return formatTime(time, "HH:mm")
    }
}