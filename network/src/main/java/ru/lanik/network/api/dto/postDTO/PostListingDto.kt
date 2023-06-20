package ru.lanik.network.api.dto.postDTO

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class PostListingDto(
    val data: PostListingDataDto,
) {
    @Serializable
    data class PostListingDataDto(
        val after: String?,
        val children: List<PostDto>,
        @Contextual
        val before: Any?,
    )
}
