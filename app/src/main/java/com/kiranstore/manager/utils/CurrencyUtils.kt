package com.kiranstore.manager.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val inrFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    fun Double.toRupees(): String = inrFormat.format(this)
    fun Double.toRupeesShort(): String {
        return when {
            this >= 1_00_000 -> "₹${String.format("%.1f", this / 1_00_000)}L"
            this >= 1_000 -> "₹${String.format("%.1f", this / 1_000)}k"
            else -> "₹${this.toLong()}"
        }
    }
}
