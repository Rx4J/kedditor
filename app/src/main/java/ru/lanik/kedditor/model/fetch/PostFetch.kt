package ru.lanik.kedditor.model.fetch

import ru.lanik.kedditor.model.source.PostSource
import ru.lanik.network.models.Post

data class PostFetch(
    val source: PostSource,
    val posts: List<Post>,
    val isUpdate: Boolean,
)
