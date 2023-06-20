package ru.lanik.network.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.lanik.network.api.dto.subredditDTO.SubredditDto
import ru.lanik.network.api.dto.subredditDTO.SubredditListingDto

interface SubredditsAPI {
    @GET("/subreddits/{source}")
    fun getSubredditListing(
        @Path("source") source: String,
        @Query("after") page: String = "",
    ): Single<SubredditListingDto>

    @GET("/r/{source}/about{auth}")
    fun getSubredditInfo(
        @Path("source") source: String?,
        @Path("auth") auth: String = "",
    ): Single<SubredditDto>

    @GET("/api/{command}")
    fun getSubredditsByName(
        @Path("command") command: String,
        @Query("query") query: String = "",
        @Query("limit") limit: Int = 5,
    ): Single<SubredditListingDto>
}