package com.kiranstore.manager.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// ── Currency Formatting ─────────────────────────────────
fun Double.toRupees(): String {
    val fmt = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return fmt.format(this).replace("₹", "₹")
}

fun Double.toRupeesShort(): String {
    return when {
        this >= 100_000 -> "₹${"%.1f".format(this / 100_000)}L"
        this >= 1_000   -> "₹${"%.1f".format(this / 1_000)}K"
        else            -> "₹${this.toInt()}"
    }
}

// ── Date Formatting ─────────────────────────────────────
fun Long.toDisplayDate(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.toDisplayDateTime(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.toRelativeTime(): String {
    val now = System.currentTimeMillis()
    val diff = now - this
    return when {
        diff < 60_000            -> "Just now"
        diff < 3_600_000         -> "${diff / 60_000} min ago"
        diff < 86_400_000        -> "${diff / 3_600_000} hours ago"
        diff < 172_800_000       -> "Yesterday"
        else                     -> toDisplayDate()
    }
}

fun todayStartMs(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun todayEndMs(): Long = todayStartMs() + 86_399_999L

// ── Avatar helpers ──────────────────────────────────────
fun String.initials(): String {
    val words = trim().split(" ")
    return when {
        words.size >= 2 -> "${words[0].firstOrNull() ?: ""}${words[1].firstOrNull() ?: ""}".uppercase()
        words.isNotEmpty() -> words[0].take(2).uppercase()
        else -> "?"
    }
}

val avatarColors = listOf(
    0xFFE8490F.toInt(), // orange
    0xFF2196F3.toInt(), // blue
    0xFF4CAF50.toInt(), // green
    0xFF9C27B0.toInt(), // purple
    0xFF607D8B.toInt(), // grey-blue
    0xFFFF5722.toInt(), // deep orange
    0xFF009688.toInt(), // teal
)

fun String.avatarColor(): Int {
    return avatarColors[this.hashCode().and(0x7FFFFFFF) % avatarColors.size]
}
