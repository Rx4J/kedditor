package ru.lanik.kedditor.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.kedditor.model.PostModel
import ru.lanik.kedditor.model.fetch.PostFetch
import ru.lanik.kedditor.model.fetch.SubredditFetch
import ru.lanik.kedditor.model.source.PostSource
import ru.lanik.kedditor.model.source.SubredditSource
import ru.lanik.kedditor.repository.PostRepository
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.network.constants.DefaultPostSort
import ru.lanik.network.models.Post
import java.net.UnknownHostException

class MainViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val postRepository: PostRepository.Reactive,
    private val subredditsRepository: SubredditsRepository.Reactive,
    private val navController: NavController,
    initSort: DefaultPostSort,
    initSubredditSource: String,
    initSource: String,
) : ViewModel() {
    private var defaultPath = PostSource(
        mainSrc = initSource,
        sortType = initSort,
    )
    private val _mainViewState: MutableStateFlow<PostModel> by lazy {
        val data = MutableStateFlow(PostModel(isLoading = true))
        postRepository.postFetchData
            .subscribe({ newValue ->
                data.value = onPostSubscribe(newValue).copy(
                    subreddits = data.value.subreddits,
                )
            }, {
                onError(it)
                data.value.isLoading = false
            }).addTo(compositeDisposable)
        subredditsRepository.subredditFetchData.subscribe({ newValue ->
            data.value = onSubredditSubscribe(newValue).copy(
                posts = data.value.posts,
                isLoading = data.value.isLoading,
                errorState = data.value.errorState,
            )
        }, {
            onError(it)
        }).addTo(compositeDisposable)
        return@lazy data
    }
    val mainViewState: StateFlow<PostModel> = _mainViewState.asStateFlow()

    init {
        if (mainViewState.value.posts == null) {
            postRepository.fetchPosts(defaultPath, "")
        }
        if (mainViewState.value.subreddits == null) {
            subredditsRepository.fetchSubreddits(SubredditSource(initSubredditSource))
        }
    }

    fun getSource(): String {
        return defaultPath.mainSrc
    }

    fun getSort(): String {
        return defaultPath.sortToStr()
    }

    fun fetchPosts(
        newSource: String? = null,
        newSort: DefaultPostSort? = null,
    ) {
        newSource?.let {
            if (it != defaultPath.mainSrc) {
                defaultPath = defaultPath.copy(
                    mainSrc = it,
                )
                setIsLoading(true)
            }
        }
        newSort?.let {
            if (it != defaultPath.sortType) {
                defaultPath = defaultPath.copy(
                    sortType = it,
                )
                setIsLoading(true)
            }
        }
        var afterId = ""
        _mainViewState.value.posts?.let {
            if (it.isNotEmpty() && !_mainViewState.value.isLoading) {
                afterId = it.last().id
            }
        }
        postRepository.fetchPosts(defaultPath, afterId)
    }

    fun onNavigateTo(commandId: Int) {
        navController.navigate(commandId)
    }

    private fun onError(error: Throwable) {
        if (error is UnknownHostException) {
            setErrorType(DefaultError.NO_INTERNET)
        } else if (error.message!!.contains("HTTP 404")) {
            setErrorType(DefaultError.UNKNOWN_HOST)
        } else if (error.message!!.contains("HTTP 403")) {
            setErrorType(DefaultError.PRIVATE)
        } else { error.printStackTrace() }
    }

    private fun onPostSubscribe(newValue: PostFetch): PostModel {
        val newList = mutableListOf<Post>()
        if (newValue.isUpdate) {
            _mainViewState.value.posts?.let {
                newList.addAll(it)
            }
        }
        newList.addAll(newValue.posts)
        return PostModel(
            posts = newList,
            errorState = DefaultError.NO,
            isLoading = false,
        )
    }

    private fun onSubredditSubscribe(newValue: SubredditFetch): PostModel {
        return PostModel(
            subreddits = newValue.subredditList,
        )
    }

    private fun setIsLoading(isLoading: Boolean) {
        if (_mainViewState.value.isLoading != isLoading) {
            _mainViewState.value = _mainViewState.value.copy(
                isLoading = isLoading,
            )
        }
    }

    private fun setErrorType(errorType: DefaultError?) {
        if (_mainViewState.value.errorState != errorType) {
            _mainViewState.value = _mainViewState.value.copy(
                errorState = errorType,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}