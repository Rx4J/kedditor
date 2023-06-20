package ru.lanik.kedditor.model

import androidx.compose.ui.graphics.vector.ImageVector

data class ActionVariant(
    val icon: ImageVector,
    val command: () -> Unit = {},
)
