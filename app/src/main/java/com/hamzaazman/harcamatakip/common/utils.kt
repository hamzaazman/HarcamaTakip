package com.hamzaazman.harcamatakip.common

import java.text.NumberFormat
import java.util.*

fun getFormattedNumber(number: String): String? {
    return if (!number.isEmpty()) {
        val `val` = number.toDouble()
        NumberFormat.getNumberInstance(Locale.getDefault()).format(`val`)
    } else {
        "0"
    }
}