package ru.lanik.network.api.dto.commentsDTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class CommentsDto(
    val data: CommentsDataDto,
) {
    @Serializable
    data class CommentsDataDto(
        val id: String,
        val subreddit: String? = null,
        val author: String? = null,
        @SerialName("created_utc")
        val createdUtc: Double? = null,
        @SerialName("body")
        val bodyComments: String? = null,
        val ups: Int? = null,
        val replies: JsonElement? = null,
    )
}
