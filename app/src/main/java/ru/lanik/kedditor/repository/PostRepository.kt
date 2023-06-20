package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.lanik.kedditor.model.fetch.PostFetch
import ru.lanik.kedditor.model.source.PostSource

interface PostRepository {
    interface Reactive {
        val postFetchData: ReplaySubject<PostFetch>
        fun fetchPosts(
            source: PostSource,
            after: String = "",
        )
        fun handleError(error: Throwable)
    }
}