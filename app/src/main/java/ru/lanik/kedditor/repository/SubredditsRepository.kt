package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.lanik.kedditor.model.fetch.SubredditFetch
import ru.lanik.kedditor.model.source.SubredditSource
import ru.lanik.network.models.Subreddit

interface SubredditsRepository {
    interface Reactive {
        val subredditFetchData: ReplaySubject<SubredditFetch>
        fun fetchSubreddits(
            source: SubredditSource,
            page: String = "",
        )

        fun getSubredditInfo(
            source: SubredditSource,
        ): Single<Subreddit>

        fun getSubredditsByName(
            query: String,
            limit: Int,
        )
        fun handleError(error: Throwable)
    }
}