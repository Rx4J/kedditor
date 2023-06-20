package ru.lanik.kedditor.model

import ru.lanik.network.models.Post

data class PostFetch(
    val source: PostSource,
    val posts: List<Post>,
    val isUpdate: Boolean,
)
