package ru.lanik.kedditor.model.fetch

import ru.lanik.kedditor.model.source.SubredditSource
import ru.lanik.network.models.Subreddit

data class SubredditFetch(
    val source: SubredditSource,
    val subredditList: List<Subreddit>,
    val isSearch: Boolean = false,
)
