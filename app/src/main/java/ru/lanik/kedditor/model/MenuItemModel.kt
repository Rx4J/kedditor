package ru.lanik.kedditor.model

data class MenuItemModel(
    val title: String,
    val currentIndex: Int = 0,
    val values: List<String>,
)
