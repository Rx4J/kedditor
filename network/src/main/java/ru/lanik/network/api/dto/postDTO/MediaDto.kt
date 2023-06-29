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
        val bitrate: Int? = null,
        @SerialName("fallback_url")
        val fallbackUrl: String? = null,
        val height: Int? = null,
        val width: Int? = null,
        @SerialName("scrubber_media_url")
        val scrubberMediaUrl: String? = null,
        @SerialName("dash_url")
        val dashUrl: String? = null,
        val duration: Int? = null,
        @SerialName("hls_url")
        val hlsUrl: String? = null,
        @SerialName("is_gif")
        val isGif: Boolean? = null,
        @SerialName("transcoding_status")
        val transcodingStatus: String? = null,
    )
}
