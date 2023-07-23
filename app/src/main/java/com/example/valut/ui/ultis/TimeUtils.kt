package com.example.valut.ui.ultis

import android.media.MediaPlayer
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Formatter
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {
    private const val DATE_FORMAT = "dd/MM/yyyy hh:mm"
    fun getVideoOrAudioFileDuration(filePath: String?): Int {
        val mediaPlayer = MediaPlayer()
        var duration = 0
        try {
            mediaPlayer.setDataSource(filePath)
            duration = mediaPlayer.duration
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (mediaPlayer != null) {
                mediaPlayer.release()
            }
        }
        return duration
    }



    fun getDateFormat(createTimeMillis: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(createTimeMillis)
    }

    fun convertMillisecondToTime(millisecond: Long?): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millisecond!!) -
                    TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            millisecond
                        )
                    ),  // The change is in this line
            TimeUnit.MILLISECONDS.toSeconds(millisecond) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millisecond
                        )
                    )
        )
    }

    fun formatElapsedTime(recycle: StringBuilder?, elapsedSeconds: Long): String {
        // Break the elapsed seconds into hours, minutes, and seconds.
        var elapsedSeconds = elapsedSeconds
        var hours: Long = 0
        var minutes: Long = 0
        var seconds: Long = 0
        if (elapsedSeconds >= 3600) {
            hours = elapsedSeconds / 3600
            elapsedSeconds -= hours * 3600
        }
        if (elapsedSeconds >= 60) {
            minutes = elapsedSeconds / 60
            elapsedSeconds -= minutes * 60
        }
        seconds = elapsedSeconds

        // Create a StringBuilder if we weren't given one to recycle.
        // TODO: if we cared, we could have a thread-local temporary StringBuilder.
        var sb = recycle
        if (sb == null) {
            sb = StringBuilder(8)
        } else {
            sb.setLength(0)
        }

        // Format the broken-down time in a locale-appropriate way.
        // TODO: use icu4c when http://unicode.org/cldr/trac/ticket/3407 is fixed.
        val f = Formatter(sb, Locale.getDefault())
        return if (hours > 0) {
            f.format("%1$02d:%2$02d:%3$02d", hours, minutes, seconds).toString()
        } else {
            f.format("%1$02d:%2$02d", minutes, seconds).toString()
        }
    }

    fun compareTimeWithCurrentTime(time: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        val timeDiffMillis = currentTimeMillis - time
        val seconds = timeDiffMillis / 1000 % 60
        val minutes = timeDiffMillis / (1000 * 60) % 60
        val hours = timeDiffMillis / (1000 * 60 * 60) % 24
        val days = timeDiffMillis / (1000 * 60 * 60 * 24) % 30
        val months = timeDiffMillis / (1000L * 60 * 60 * 24 * 30) % 12
        val years = timeDiffMillis / (1000L * 60 * 60 * 24 * 30 * 12)
        val result: String
        result = if (years > 0) {
            "$years year before"
        } else if (months > 0) {
            "$months month before"
        } else if (days > 0) {
            "$days day before"
        } else if (hours > 0) {
            "$hours hour before"
        } else if (minutes > 0) {
            "$minutes minute before"
        } else {
            "$seconds second before "
        }
        println("Kết quả: $result")
        return result
    }
}