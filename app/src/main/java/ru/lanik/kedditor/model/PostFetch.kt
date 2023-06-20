package ru.lanik.kedditor.model

import ru.lanik.network.models.Post

data class PostFetch(
    val postSrc: PostSource,
    val posts: List<Post>,
    val isUpdate: Boolean,
)
