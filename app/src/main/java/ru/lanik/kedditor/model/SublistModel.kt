package ru.lanik.kedditor.model

import ru.lanik.network.models.Subreddit

data class SublistModel(
    val subreddits: List<Subreddit>? = null,
    val subredditSearch: List<Subreddit>? = null,
)
