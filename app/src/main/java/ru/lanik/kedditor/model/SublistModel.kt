package ru.lanik.kedditor.model

import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.network.models.Subreddit

data class SublistModel(
    val subreddits: List<Subreddit>? = null,
    val subredditSearch: List<Subreddit>? = null,
    var errorState: DefaultError? = null,
    var isLoading: Boolean = false,
)
