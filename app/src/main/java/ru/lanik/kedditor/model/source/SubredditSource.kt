package ru.lanik.kedditor.model.source

import ru.lanik.network.constants.DefaultSubredditSource

data class SubredditSource(
    val mainSrc: String,
) {
    companion object {
        fun fromEnum(src: DefaultSubredditSource): SubredditSource {
            return SubredditSource(src.name.lowercase())
        }
    }
}