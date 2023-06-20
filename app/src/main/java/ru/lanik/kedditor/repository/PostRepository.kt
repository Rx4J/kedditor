package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.lanik.kedditor.model.PostFetch
import ru.lanik.kedditor.model.PostSource

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