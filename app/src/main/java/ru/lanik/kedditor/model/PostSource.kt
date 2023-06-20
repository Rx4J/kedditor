package ru.lanik.kedditor.model

import ru.lanik.network.constants.DefaultPostSort

data class PostSource(
    val mainSrc: String,
    val sortType: DefaultPostSort,
) {
    fun toPath(): String {
        return "$mainSrc/${sortType.name.lowercase()}"
    }
}