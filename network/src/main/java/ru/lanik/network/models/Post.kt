package ru.lanik.network.models

import kotlinx.coroutines.flow.MutableStateFlow
import ru.lanik.network.extension.toFullLong
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Post(
    val selfText: String?,
    val saved: Boolean,
    val title: String,
    val subredditNamePrefixed: String,
    override val name: String,
    val score: Int,
    val created: Double,
    val subredditId: String,
    override val id: String,
    val author: String,
    val numComments: Int,
    val permalink: String,
    val url: String,
    val fallbackUrl: String?,
    val isVideo: Boolean,
    val likedByUser: Boolean?,
    var dir: Int,
    val thumbnail: String?,
    val iconUrl: MutableStateFlow<String> = MutableStateFlow(""),
) : ListItem {
    fun getDate(): String {
        try {
            val calendar: Calendar = Calendar.getInstance()
            val tz: TimeZone = TimeZone.getDefault()
            calendar.timeInMillis = created.toFullLong() * 1000
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.US)
            val currentTimeZone: Date = calendar.time as Date
            return sdf.format(currentTimeZone)
        } catch (_: Exception) {
        }
        return ""
    }
}
