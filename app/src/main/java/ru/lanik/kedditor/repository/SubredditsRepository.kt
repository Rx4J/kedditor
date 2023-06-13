package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.lanik.kedditor.model.SublistModel
import ru.lanik.network.constants.ApiSubredditSource

interface SubredditsRepository {
    interface Reactive {
        val postFetchData: ReplaySubject<SublistModel>
        fun fetchSubreddits(
            source: ApiSubredditSource,
            isAuth: Boolean,
            page: String = "",
        )
        fun getSubredditsByName(
            isAuth: Boolean,
            query: String,
            limit: Int,
        )
        fun handleError(error: Throwable)
    }
}