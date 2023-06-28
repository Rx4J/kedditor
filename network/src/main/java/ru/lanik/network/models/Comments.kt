package ru.lanik.network.models

import ru.lanik.network.api.dto.commentsDTO.CommentsListingDto
import ru.lanik.network.extension.toFullLong
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Comments(
    override val id: String,
    override val name: String,
    val author: String?,
    val ups: Int?,
    val utcTimeStamp: Double,
    val commentsBody: String?,
    val replies: List<CommentsListingDto>?,
) : ListItem {
    fun getDate(): String {
        try {
            val calendar: Calendar = Calendar.getInstance()
            val tz: TimeZone = TimeZone.getDefault()
            calendar.timeInMillis = utcTimeStamp.toFullLong() * 1000
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.US)
            val currentTimeZone: Date = calendar.time as Date
            return sdf.format(currentTimeZone)
        } catch (_: Exception) {
        }
        return ""
    }
}
