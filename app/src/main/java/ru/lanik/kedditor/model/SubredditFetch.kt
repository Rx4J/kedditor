package ru.lanik.kedditor.model

import ru.lanik.network.models.Subreddit

data class SubredditFetch(
    val source: SubredditSource,
    val subredditList: List<Subreddit>,
    val isSearch: Boolean = false,
)
