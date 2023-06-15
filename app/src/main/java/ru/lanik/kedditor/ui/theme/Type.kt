package ru.lanik.kedditor.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

data class KedditorTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle,
)

val LocalKedditorTypography = staticCompositionLocalOf<KedditorTypography> {
    error("No typography provided")
}
