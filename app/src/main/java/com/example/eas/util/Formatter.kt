package com.example.eas.util

import java.text.NumberFormat
import java.util.Locale

object Formatter {

    fun formatRupiah(amount: Long): String {
        val nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
        nf.maximumFractionDigits = 0
        return "Rp${nf.format(amount)}"
    }

    fun formatMemberId(id: Int): String = "MBR%05d".format(id)

    fun memberLevel(points: Int): String = when {
        points >= 250 -> "Platinum"
        points >= 100 -> "Gold"
        else -> "Silver"
    }
}
