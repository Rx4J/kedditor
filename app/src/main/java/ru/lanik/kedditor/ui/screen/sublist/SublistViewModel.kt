package ru.lanik.kedditor.ui.screen.sublist

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.kedditor.model.SublistModel
import ru.lanik.kedditor.model.fetch.SubredditFetch
import ru.lanik.kedditor.model.source.SubredditSource
import ru.lanik.kedditor.repository.SettingsManager
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.network.constants.DefaultSubredditSource
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SublistViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val subredditsRepository: SubredditsRepository.Reactive,
    private val navController: NavController,
    private val settingsManager: SettingsManager.Reactive,
) : ViewModel() {
    private val settingsStateFlow = settingsManager.getStateFlow()
    private var defaultPath = SubredditSource(
        mainSrc = settingsStateFlow.value.defaultSubredditSource.name.lowercase(),
    )
    private val _sublistViewState: MutableStateFlow<SublistModel> by lazy {
        val data = MutableStateFlow(SublistModel(isLoading = true))
        subredditsRepository.subredditFetchData.subscribe({ newValue ->
            data.value = onSubredditSubscribe(newValue)
        }, {
            onError(it)
            data.value.isLoading = false
        }).addTo(compositeDisposable)
        return@lazy data
    }
    val sublistViewState: StateFlow<SublistModel> = _sublistViewState.asStateFlow()

    init {
        if (_sublistViewState.value.subreddits == null) {
            subredditsRepository.fetchSubreddits(defaultPath)
        }
    }

    fun onSearching(str: String) {
        subredditsRepository.getSubredditsByName(
            query = str,
            limit = 5,
        )
    }

    fun onNavigateBack() {
        navController.navigateUp()
    }

    fun fetchSubreddits(
        newSource: DefaultSubredditSource? = null,
    ) {
        newSource?.let {
            if (it.name.lowercase() != defaultPath.mainSrc) {
                defaultPath = SubredditSource.fromEnum(it)
            }
        }
        var afterId = ""
        _sublistViewState.value.subreddits?.let {
            if (it.isNotEmpty() && !_sublistViewState.value.isLoading) {
                afterId = it.last().id
            }
        }
        subredditsRepository.fetchSubreddits(defaultPath, afterId)
    }

    private fun onSubredditSubscribe(newValue: SubredditFetch): SublistModel {
        val newList = if (newValue.isSearch) newValue.subredditList else newValue.subredditList
        return SublistModel(
            subreddits = newList,
            errorState = DefaultError.NO,
            isLoading = false,
        )
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

    private fun setErrorType(errorType: DefaultError?) {
        if (_sublistViewState.value.errorState != errorType) {
            _sublistViewState.value.errorState = errorType
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}