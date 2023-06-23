package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.lanik.kedditor.model.fetch.PostFetch
import ru.lanik.kedditor.model.source.PostSource
import ru.lanik.kedditor.utils.SchedulerPolicy
import ru.lanik.kedditor.utils.extension.applySchedulerPolicy
import ru.lanik.kedditor.utils.extension.fixAuth
import ru.lanik.network.api.PostAPI
import ru.lanik.network.api.SubredditsAPI
import ru.lanik.network.extension.toListPost
import ru.lanik.network.extension.toSubreddit
import ru.lanik.network.models.Post

class RxPostRepository(
    private val postAPI: PostAPI,
    private val subredditsAPI: SubredditsAPI,
    private val schedulerPolicy: SchedulerPolicy,
    private val compositeDisposable: CompositeDisposable,
) : PostRepository.Reactive {
    override val postFetchData: ReplaySubject<PostFetch> = ReplaySubject.create(1)

    override fun fetchPosts(
        source: PostSource,
        after: String,
    ) {
        val direct = source.toPath().fixAuth(false)
        postAPI.getPosts(direct, after)
            .applySchedulerPolicy(schedulerPolicy)
            .concatMap { dto ->
                val postList = dto.data.children.toListPost()
                val postsWithImage = postList.map { post ->
                    subredditsAPI.getSubredditInfo(post.name, "".fixAuth(false)).map { info ->
                        post.copy(
                            iconUrl = info.toSubreddit().imageUrl ?: "",
                        )
                    }
                }
                return@concatMap Single.zip(postsWithImage) { updatedPosts ->
                    updatedPosts.map { it as Post }
                }
            }
            .subscribe({ data ->
                postFetchData.onNext(
                    PostFetch(
                        source = source,
                        posts = data,
                        isUpdate = postFetchData.value?.source == source,
                    ),
                )
            }, { error ->
                handleError(error)
            }).also { compositeDisposable.add(it) }
    }

    override fun handleError(error: Throwable) {
        postFetchData.onError(error)
    }
}