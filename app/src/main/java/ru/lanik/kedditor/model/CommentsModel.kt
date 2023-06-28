package ru.lanik.kedditor.model

import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.network.models.PostWithComments

data class CommentsModel(
    val postWithComments: PostWithComments? = null,
    var errorState: DefaultError? = null,
    var isLoading: Boolean = false,
)
