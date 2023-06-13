package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.lanik.kedditor.model.SublistModel
import ru.lanik.kedditor.utils.SchedulerPolicy
import ru.lanik.kedditor.utils.extension.applySchedulerPolicy
import ru.lanik.network.api.SubredditsAPI
import ru.lanik.network.constants.ApiBaseConst
import ru.lanik.network.constants.ApiSubredditSource
import ru.lanik.network.extension.toListSubreddit

class RxSubredditsRepository(
    private val subredditsAPI: SubredditsAPI,
    private val schedulerPolicy: SchedulerPolicy,
    private val compositeDisposable: CompositeDisposable,
) : SubredditsRepository.Reactive {
    override val postFetchData: ReplaySubject<SublistModel> = ReplaySubject.create()

    override fun fetchSubreddits(
        source: ApiSubredditSource,
        isAuth: Boolean,
        page: String,
    ) {
        val direct = if (isAuth) source.auth else source.notAuth
        subredditsAPI.getSubredditListing(direct, page)
            .applySchedulerPolicy(schedulerPolicy)
            .subscribe({ data ->
                val subredditList = data.data.children.toListSubreddit()
                postFetchData.onNext(
                    postFetchData.value?.copy(
                        subreddits = subredditList,
                    ) ?: SublistModel(subreddits = subredditList, null),
                )
            }, { error ->
                handleError(error)
            }).also { compositeDisposable.add(it) }
    }

    override fun getSubredditsByName(
        isAuth: Boolean,
        query: String,
        limit: Int,
    ) {
        val cmd = if (isAuth) {
            ApiBaseConst.SEARCH_SUBREDDIT.auth
        } else {
            ApiBaseConst.SEARCH_SUBREDDIT.notAuth
        }
        subredditsAPI.getSubredditsByName(cmd, query, limit)
            .applySchedulerPolicy(schedulerPolicy)
            .subscribe({ data ->
                val subredditList = data.data.children.toListSubreddit()
                postFetchData.onNext(
                    postFetchData.value?.copy(
                        subredditSearch = subredditList,
                    ) ?: SublistModel(null, subredditList),
                )
            }, { error ->
                handleError(error)
            }).also { compositeDisposable.add(it) }
    }

    override fun handleError(error: Throwable) {
        // TODO: Processing error
        postFetchData.onError(error)
    }
}