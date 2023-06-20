package ru.lanik.network.api.dto.postDTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaDto(
    @SerialName("reddit_video")
    val redditVideo: RedditVideoDto? = null,
) {
    @Serializable
    data class RedditVideoDto(
        @SerialName("bitrate_kbps")
        val bitrate: Int,
        @SerialName("fallback_url")
        val fallbackUrl: String,
        val height: Int,
        val width: Int,
        @SerialName("scrubber_media_url")
        val scrubberMediaUrl: String,
        @SerialName("dash_url")
        val dashUrl: String,
        val duration: Int,
        @SerialName("hls_url")
        val hlsUrl: String,
        @SerialName("is_gif")
        val isGif: Boolean,
        @SerialName("transcoding_status")
        val transcodingStatus: String,
    )
}
