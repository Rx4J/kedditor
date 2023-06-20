package ru.lanik.kedditor.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import ru.lanik.network.models.Post
import ru.lanik.network.models.Subreddit

data class PostModel(
    val posts: SnapshotStateList<Post>? = null,
    val subreddits: List<Subreddit>? = null,
    var isLoading: Boolean = false,
)
