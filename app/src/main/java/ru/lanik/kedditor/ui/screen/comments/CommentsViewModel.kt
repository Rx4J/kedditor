package ru.lanik.kedditor.ui.screen.comments

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.kedditor.model.CommentsModel
import ru.lanik.kedditor.repository.PostRepository
import ru.lanik.kedditor.repository.SettingsManager
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CommentsViewModel(
    private val postRepository: PostRepository.Reactive,
    private val compositeDisposable: CompositeDisposable,
    private val navController: NavController,
    private val postUrl: String,
    private val settingsManager: SettingsManager.Reactive,
) : ViewModel() {
    private val settingsStateFlow = settingsManager.getStateFlow()
    private val _commentsViewState: MutableStateFlow<CommentsModel> by lazy {
        val data = MutableStateFlow(CommentsModel(isLoading = true))
        postRepository.fetchPostWithComments(postUrl).subscribe({ newValue ->
            data.value = data.value.copy(
                postWithComments = newValue,
                errorState = DefaultError.NO,
                isLoading = false,
            )
        }, {
            onError(it)
        }).addTo(compositeDisposable)
        return@lazy data
    }
    val commentsViewState: StateFlow<CommentsModel> = _commentsViewState.asStateFlow()

    fun isAuth(): Boolean = settingsStateFlow.value.isAuth

    fun onNavigateBack() {
        compositeDisposable.clear()
        navController.navigateUp()
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
        if (_commentsViewState.value.errorState != errorType) {
            _commentsViewState.value = _commentsViewState.value.copy(
                errorState = errorType,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}