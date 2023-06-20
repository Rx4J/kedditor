package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.lanik.kedditor.model.fetch.SubredditFetch
import ru.lanik.kedditor.model.source.SubredditSource

interface SubredditsRepository {
    interface Reactive {
        val subredditFetchData: ReplaySubject<SubredditFetch>
        fun fetchSubreddits(
            source: SubredditSource,
            page: String = "",
        )
        fun getSubredditsByName(
            query: String,
            limit: Int,
        )
        fun handleError(error: Throwable)
    }
}