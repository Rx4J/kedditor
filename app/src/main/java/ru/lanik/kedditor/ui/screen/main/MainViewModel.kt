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
import ru.lanik.kedditor.model.source.PostSource
import ru.lanik.kedditor.model.source.SubredditSource
import ru.lanik.kedditor.repository.PostRepository
import ru.lanik.kedditor.repository.SettingsManager
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.network.constants.DefaultPostSort
import ru.lanik.network.models.Post
import ru.lanik.network.models.Subreddit
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
    private var defaultPostPath = PostSource(
        mainSrc = settingsStateFlow.value.defaultPostSource.name.lowercase(),
        sortType = settingsStateFlow.value.defaultPostSort,
    )
    private var defaultSubredditPath = SubredditSource(
        mainSrc = settingsStateFlow.value.defaultSubredditSource.name.lowercase(),
    )
    private val _mainViewState = MutableStateFlow(PostModel(isLoading = true))
    val mainViewState: StateFlow<PostModel> = _mainViewState.asStateFlow()

    fun getSource(): String {
        return defaultPostPath.mainSrc
    }

    fun getSort(): String {
        return defaultPostPath.sortToStr()
    }

    fun setSource(newSource: String) {
        defaultPostPath = PostSource(
            mainSrc = newSource,
            sortType = settingsStateFlow.value.defaultPostSort,
        )
    }

    fun fetchPosts(
        newSource: String? = null,
        newSort: DefaultPostSort? = null,
    ) {
        newSource?.let {
            if (it != defaultPostPath.mainSrc) {
                defaultPostPath = defaultPostPath.copy(
                    mainSrc = it,
                )
                setIsLoading(true)
            }
        }
        newSort?.let {
            if (it != defaultPostPath.sortType) {
                defaultPostPath = defaultPostPath.copy(
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
        postRepository.fetchPosts(defaultPostPath, afterId).subscribe({
            _mainViewState.value = onPostSubscribe(
                newValue = it,
                isUpdate = (newSource == null && newSort == null)
            )
        }, {
            onError(it)
            setIsLoading(false)
        }).addTo(compositeDisposable)
    }

    fun fetchPostsForUpdate() {
        fetchPosts(null, null)
    }

    fun fetchSubreddits() {
        subredditsRepository.fetchSubreddits(defaultSubredditPath, "").subscribe({
            _mainViewState.value = onSubredditSubscribe(it)
        }, {
            onError(it)
        }).addTo(compositeDisposable)
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

    private fun onPostSubscribe(
        newValue: List<Post>,
        isUpdate: Boolean,
    ): PostModel {
        val newList = mutableListOf<Post>()
        if (isUpdate) {
            _mainViewState.value.posts?.let {
                newList.addAll(it)
            }
        }
        newList.addAll(newValue)
        return mainViewState.value.copy(
            posts = newList,
            errorState = DefaultError.NO,
            isLoading = false,
        )
    }

    private fun onSubredditSubscribe(newValue: List<Subreddit>): PostModel {
        return mainViewState.value.copy(
            subreddits = newValue,
            errorState = DefaultError.NO,
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