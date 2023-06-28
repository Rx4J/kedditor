package ru.lanik.network.api.dto.commentsDTO

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class CommentsListingDto(
    val data: CommentsListingDataDto,
) {
    @Serializable
    data class CommentsListingDataDto(
        val after: String?,
        val children: List<CommentsDto>,
        @Contextual
        val before: Any?,
    )
}