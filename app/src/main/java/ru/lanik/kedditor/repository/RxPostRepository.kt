package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.flow.StateFlow
import ru.lanik.kedditor.model.SettingsModel
import ru.lanik.kedditor.model.source.PostSource
import ru.lanik.kedditor.utils.SchedulerPolicy
import ru.lanik.kedditor.utils.extension.applySchedulerPolicy
import ru.lanik.kedditor.utils.extension.fixAuth
import ru.lanik.network.api.PostAPI
import ru.lanik.network.api.SubredditsAPI
import ru.lanik.network.extension.toListPost
import ru.lanik.network.extension.toPostWithComments
import ru.lanik.network.extension.toSubreddit
import ru.lanik.network.models.Post
import ru.lanik.network.models.PostWithComments

class RxPostRepository(
    private val postAPI: PostAPI,
    private val subredditsAPI: SubredditsAPI,
    private val schedulerPolicy: SchedulerPolicy,
    private val settingsStateFlow: StateFlow<SettingsModel>,
) : PostRepository.Reactive {
    override fun fetchPosts(
        source: PostSource,
        after: String,
    ): Single<List<Post>> {
        val compositeDisposable = CompositeDisposable()
        val direct = source.toPath().fixAuth(settingsStateFlow.value.isAuth)
        return postAPI.getPosts(direct, after)
            .applySchedulerPolicy(schedulerPolicy)
            .doOnDispose { compositeDisposable.dispose() }
            .map { dto ->
                val postList = dto.data.children.toListPost()
                postList.forEach { post ->
                    subredditsAPI.getSubredditInfo(post.name, "".fixAuth(settingsStateFlow.value.isAuth)).subscribe({
                        post.iconUrl.value = it.toSubreddit().imageUrl ?: ""
                    }, {}).addTo(compositeDisposable)
                }
                return@map postList
            }
    }

    override fun fetchPostWithComments(url: String): Single<PostWithComments> {
        val newUrl = url.drop(1).dropLast(1)
        val direct = newUrl.fixAuth(settingsStateFlow.value.isAuth)
        return postAPI.getSinglePost(direct).map {
            it.toPostWithComments()
        }
    }
}