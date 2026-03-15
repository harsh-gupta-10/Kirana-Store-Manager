package com.kiranstore.manager.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val displayFmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFmt = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())
    private val dayFmt = SimpleDateFormat("dd MMM", Locale.getDefault())

    fun Long.toDisplayDate(): String = displayFmt.format(Date(this))
    fun Long.toDisplayDateTime(): String = timeFmt.format(Date(this))
    fun Long.toDayMonth(): String = dayFmt.format(Date(this))

    fun todayStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun todayEnd(): Long = todayStart() + 86_399_999L

    fun Long.isToday(): Boolean {
        val start = todayStart(); return this in start..todayEnd()
    }

    fun Long.timeAgo(): String {
        val diff = System.currentTimeMillis() - this
        return when {
            diff < 60_000 -> "just now"
            diff < 3_600_000 -> "${diff / 60_000} min ago"
            diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
            diff < 172_800_000 -> "Yesterday"
            else -> this.toDisplayDate()
        }
    }
}
