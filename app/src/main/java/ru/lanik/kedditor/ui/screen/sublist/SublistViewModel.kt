package ru.lanik.kedditor.ui.screen.sublist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.kedditor.model.SublistModel
import ru.lanik.kedditor.model.source.SubredditSource
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.network.constants.DefaultSubredditSource
import java.net.UnknownHostException

class SublistViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val subredditsRepository: SubredditsRepository.Reactive,
    private val navController: NavController,
    initSource: String,
) : ViewModel() {
    private var defaultPath = SubredditSource(
        mainSrc = initSource,
    )
    private val _sublistViewState: MutableStateFlow<SublistModel> by lazy {
        val data = MutableStateFlow(SublistModel(isLoading = true))
        subredditsRepository.subredditFetchData.subscribe({ newValue ->
            if (newValue.isSearch) {
                data.value = data.value.copy(
                    subredditSearch = newValue.subredditList,
                    errorState = DefaultError.NO,
                    isLoading = false,
                )
            } else {
                data.value = data.value.copy(
                    subreddits = newValue.subredditList,
                    errorState = DefaultError.NO,
                    isLoading = false,
                )
            }
        }, {
            onError(it)
            data.value.isLoading = false
        }).also { compositeDisposable.add(it) }
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
        Log.e("Deb", navController.backQueue.size.toString())
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
        sublistViewState.value.subreddits?.let {
            if (it.isNotEmpty() && newSource == null) {
                afterId = it.last().id
            }
        }
        subredditsRepository.fetchSubreddits(defaultPath, afterId)
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