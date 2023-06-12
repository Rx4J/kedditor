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
        val displayName: String,
        @SerialName("display_name_prefixed")
        val displayNamePrefixed: String,
        val subscribers: Int?,
        val name: String,
        @SerialName("icon_img")
        val iconImg: String?,
        @SerialName("community_icon")
        val iconCommunity: String?,
        val created: Double?,
        @SerialName("user_is_subscriber")
        val isSubscriber: Boolean?,
        @SerialName("header_title")
        val description: String?,
        val url: String?,
    )
}