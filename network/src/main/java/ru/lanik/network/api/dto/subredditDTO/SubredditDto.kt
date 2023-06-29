package ru.lanik.network.api.dto.subredditDTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditDto(
    val data: SubredditDataDto,
) {
    @Serializable
    data class SubredditDataDto(
        @SerialName("display_name")
        val displayName: String? = null,
        @SerialName("display_name_prefixed")
        val displayNamePrefixed: String? = null,
        val subscribers: Int? = null,
        val name: String? = null,
        @SerialName("icon_img")
        val iconImg: String? = null,
        @SerialName("community_icon")
        val iconCommunity: String? = null,
        val created: Double? = null,
        @SerialName("user_is_subscriber")
        val isSubscriber: Boolean? = null,
        @SerialName("header_title")
        val description: String? = null,
        val url: String? = null,
    )
}