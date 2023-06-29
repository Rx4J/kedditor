package ru.lanik.network.api.dto.postDTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val data: PostDataDto,
) {
    @Serializable
    data class PostDataDto(
        val subreddit: String? = null,
        @SerialName("selftext")
        val selfText: String? = null,
        val saved: Boolean? = null,
        val title: String? = null,
        @SerialName("subreddit_name_prefixed")
        val subredditNamePrefixed: String? = null,
        val name: String? = null,
        val score: Int? = null,
        val thumbnail: String? = null,
        val created: Double? = null,
        @SerialName("subreddit_id")
        val subredditId: String? = null,
        val id: String? = null,
        val author: String? = null,
        @SerialName("num_comments")
        val numComments: Int? = null,
        val permalink: String? = null,
        val url: String? = null,
        val media: MediaDto? = null,
        @SerialName("is_video")
        val isVideo: Boolean? = null,
        val likes: Boolean? = null,
    )
}
