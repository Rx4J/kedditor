package ru.lanik.network.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.lanik.network.api.dto.postDTO.PostListingDto

interface PostAPI {
    @GET("/r/{source}")
    fun getPosts(
        @Path("source") source: String?,
        @Query("after") page: String = "",
    ): Single<PostListingDto>
}