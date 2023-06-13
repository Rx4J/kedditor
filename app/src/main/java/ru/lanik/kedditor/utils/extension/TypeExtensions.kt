package ru.lanik.kedditor.utils.extension

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Int.toFormatStr(delim: String = " "): String {
    val strBuilder = DecimalFormat(
        "###,###,###,###,###",
        DecimalFormatSymbols(Locale.ENGLISH),
    )
    return strBuilder.format(this).replace(",", delim)
}