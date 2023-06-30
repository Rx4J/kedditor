package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.lanik.kedditor.model.fetch.PostFetch
import ru.lanik.kedditor.model.source.PostSource
import ru.lanik.network.models.PostWithComments

interface PostRepository {
    interface Reactive {
        val postFetchData: PublishSubject<PostFetch>
        fun fetchPosts(
            source: PostSource,
            after: String = "",
        )
        fun fetchPostWithComments(url: String): Single<PostWithComments>
        fun handleError(error: Throwable)
    }
}