package ru.lanik.network.api.dto.postDTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val data: PostDataDto,
) {
    @Serializable
    data class PostDataDto(
        val subreddit: String,
        @SerialName("selftext")
        val selfText: String?,
        val saved: Boolean,
        val title: String,
        @SerialName("subreddit_name_prefixed")
        val subredditNamePrefixed: String,
        val name: String,
        val score: Int,
        val thumbnail: String?,
        val created: Double,
        @SerialName("subreddit_id")
        val subredditId: String,
        val id: String,
        val author: String,
        @SerialName("num_comments")
        val numComments: Int,
        val permalink: String,
        val url: String,
        val media: MediaDto?,
        @SerialName("is_video")
        val isVideo: Boolean,
        val likes: Boolean?,
    )
}
