package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.StateFlow
import ru.lanik.kedditor.model.SettingsModel
import ru.lanik.kedditor.model.fetch.PostFetch
import ru.lanik.kedditor.model.source.PostSource
import ru.lanik.kedditor.utils.SchedulerPolicy
import ru.lanik.kedditor.utils.extension.applySchedulerPolicy
import ru.lanik.kedditor.utils.extension.fixAuth
import ru.lanik.network.api.PostAPI
import ru.lanik.network.api.SubredditsAPI
import ru.lanik.network.extension.toListPost
import ru.lanik.network.extension.toPostWithComments
import ru.lanik.network.extension.toSubreddit
import ru.lanik.network.models.PostWithComments
import java.util.concurrent.atomic.AtomicBoolean

class RxPostRepository(
    private val postAPI: PostAPI,
    private val subredditsAPI: SubredditsAPI,
    private val schedulerPolicy: SchedulerPolicy,
    private val settingsStateFlow: StateFlow<SettingsModel>,
    private val compositeDisposable: CompositeDisposable,
) : PostRepository.Reactive {
    override val postFetchData: PublishSubject<PostFetch> = PublishSubject.create()
    private var rememberSource: PostSource? = null
    private val loadingState: AtomicBoolean = AtomicBoolean(false)

    override fun fetchPosts(
        source: PostSource,
        after: String,
    ) {
        if (!loadingState.get()) {
            loadingState.set(true)
            val direct = source.toPath().fixAuth(settingsStateFlow.value.isAuth)
            postAPI.getPosts(direct, after)
                .applySchedulerPolicy(schedulerPolicy)
                .doAfterSuccess {
                    loadingState.set(false)
                    rememberSource = source
                }
                .map { dto ->
                    val postList = dto.data.children.toListPost()
                    postList.forEach { post ->
                        subredditsAPI.getSubredditInfo(post.name, "".fixAuth(settingsStateFlow.value.isAuth)).subscribe({
                            post.iconUrl.value = it.toSubreddit().imageUrl ?: ""
                        }, {}).addTo(compositeDisposable)
                    }
                    return@map postList
                }
                .subscribe({ data ->
                    postFetchData.onNext(
                        PostFetch(
                            source = source,
                            posts = data,
                            isUpdate = rememberSource == source,
                        ),
                    )
                }, { error ->
                    handleError(error)
                }).also { compositeDisposable.add(it) }
        }
    }

    override fun fetchPostWithComments(url: String): Single<PostWithComments> {
        val newUrl = url.drop(1).dropLast(1)
        val direct = newUrl.fixAuth(settingsStateFlow.value.isAuth)
        return postAPI.getSinglePost(direct).map {
            it.toPostWithComments()
        }
    }

    override fun handleError(error: Throwable) {
        postFetchData.onError(error)
    }
}