package ru.lanik.kedditor.ui.screen.main

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.kedditor.R
import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.kedditor.model.PostModel
import ru.lanik.kedditor.model.fetch.PostFetch
import ru.lanik.kedditor.model.fetch.SubredditFetch
import ru.lanik.kedditor.model.source.PostSource
import ru.lanik.kedditor.model.source.SubredditSource
import ru.lanik.kedditor.repository.PostRepository
import ru.lanik.kedditor.repository.SettingsManager
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.network.constants.DefaultPostSort
import ru.lanik.network.models.Post
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val postRepository: PostRepository.Reactive,
    private val subredditsRepository: SubredditsRepository.Reactive,
    private val navController: NavController,
    private val settingsManager: SettingsManager.Reactive,
) : ViewModel() {
    private val settingsStateFlow = settingsManager.getStateFlow()
    private var defaultPath = PostSource(
        mainSrc = settingsStateFlow.value.defaultPostSource.name.lowercase(),
        sortType = settingsStateFlow.value.defaultPostSort,
    )
    private val _mainViewState: MutableStateFlow<PostModel> by lazy {
        val data = MutableStateFlow(PostModel(isLoading = true))
        postRepository.postFetchData
            .subscribe({ newValue ->
                data.value = onPostSubscribe(newValue)
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
        subredditsRepository.fetchSubreddits(SubredditSource(settingsStateFlow.value.defaultSubredditSource.name.lowercase()))
        return@lazy data
    }
    val mainViewState: StateFlow<PostModel> = _mainViewState.asStateFlow()

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

    fun onNavigateToComments(url: String) {
        val bundle = bundleOf("post_url" to url)
        compositeDisposable.clear()
        navController.navigate(R.id.action_main_to_view, bundle)
    }

    fun onNavigateTo(commandId: Int) {
        compositeDisposable.clear()
        navController.navigate(commandId)
    }

    private fun onError(error: Throwable) {
        if (error is UnknownHostException) {
            setErrorType(DefaultError.NO_INTERNET)
        } else if (error.message!!.contains("HTTP 404")) {
            setErrorType(DefaultError.UNKNOWN_HOST)
        } else if (error.message!!.contains("HTTP 403")) {
            setErrorType(DefaultError.PRIVATE)
        } else if (error is SocketTimeoutException) {
            setErrorType(DefaultError.UNKNOWN_HOST)
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