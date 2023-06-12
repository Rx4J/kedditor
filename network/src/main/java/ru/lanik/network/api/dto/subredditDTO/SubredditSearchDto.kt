package ru.lanik.network.api.dto.subredditDTO

import kotlinx.serialization.Serializable

@Serializable
data class SubredditSearchDto(
    val subreddits: List<SubredditSearchDataDto>,
) {
    @Serializable
    data class SubredditSearchDataDto(
        val numSubscribers: Int,
        val name: String,
        val id: String,
        val communityIcon: String?,
    )
}
