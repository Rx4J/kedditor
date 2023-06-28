package ru.lanik.network.models

data class PostWithComments(
    val post: Post,
    val comments: List<Comments>,
)
