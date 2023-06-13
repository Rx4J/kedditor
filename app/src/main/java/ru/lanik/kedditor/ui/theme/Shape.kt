package ru.lanik.kedditor.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

data class KedditorShape(
    val generalPadding: Dp,
    val textHorizontalPadding: Dp,
    val textVerticalPadding: Dp,
    val cornersStyle: Shape,
)

enum class KedditorCorners {
    Flat, Rounded,
}

val LocalKedditorShape = staticCompositionLocalOf<KedditorShape> {
    error("No shapes provided")
}