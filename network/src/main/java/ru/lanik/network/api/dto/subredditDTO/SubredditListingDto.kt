package ru.lanik.network.api.dto.subredditDTO

import kotlinx.serialization.Serializable

@Serializable
data class SubredditListingDto(
    val data: SubredditListingDataDto,
) {
    @Serializable
    data class SubredditListingDataDto(
        val after: String?,
        val dist: Int?,
        val children: List<SubredditDto>,
        val before: String?,
    )
}