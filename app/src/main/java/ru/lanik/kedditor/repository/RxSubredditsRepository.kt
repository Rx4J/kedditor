package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.lanik.kedditor.model.fetch.SubredditFetch
import ru.lanik.kedditor.model.source.SubredditSource
import ru.lanik.kedditor.utils.SchedulerPolicy
import ru.lanik.kedditor.utils.extension.applySchedulerPolicy
import ru.lanik.kedditor.utils.extension.fixAuth
import ru.lanik.network.api.SubredditsAPI
import ru.lanik.network.constants.ApiBaseConst
import ru.lanik.network.extension.toListSubreddit

class RxSubredditsRepository(
    private val subredditsAPI: SubredditsAPI,
    private val schedulerPolicy: SchedulerPolicy,
    private val compositeDisposable: CompositeDisposable,
) : SubredditsRepository.Reactive {
    override val subredditFetchData: ReplaySubject<SubredditFetch> = ReplaySubject.create()

    override fun fetchSubreddits(
        source: SubredditSource,
        page: String,
    ) {
        val direct = source.mainSrc.fixAuth(false)
        subredditsAPI.getSubredditListing(direct, page)
            .applySchedulerPolicy(schedulerPolicy)
            .subscribe({ data ->
                val subredditList = data.data.children.toListSubreddit()
                subredditFetchData.onNext(
                    SubredditFetch(
                        source = source,
                        subredditList = subredditList,
                    )
                )
            }, { error ->
                handleError(error)
            }).also { compositeDisposable.add(it) }
    }

    override fun getSubredditsByName(
        query: String,
        limit: Int,
    ) {
        val cmd = if (true) {
            ApiBaseConst.SEARCH_SUBREDDIT.auth
        } else {
            ApiBaseConst.SEARCH_SUBREDDIT.notAuth
        }
        subredditsAPI.getSubredditsByName(cmd, query, limit)
            .applySchedulerPolicy(schedulerPolicy)
            .subscribe({ data ->
                val subredditList = data.data.children.toListSubreddit()
                subredditFetchData.onNext(
                    SubredditFetch(
                        source = SubredditSource(query),
                        subredditList = subredditList,
                        isSearch = true,
                    )
                )
            }, { error ->
                handleError(error)
            }).also { compositeDisposable.add(it) }
    }

    override fun handleError(error: Throwable) {
        // TODO: Processing error
        subredditFetchData.onError(error)
    }
}