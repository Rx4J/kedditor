package ru.lanik.kedditor.ui.screen.sublist

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.kedditor.model.SublistModel
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.network.constants.ApiSubredditSource

class SublistViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val subredditsRepository: SubredditsRepository.Reactive,
    private val navController: NavController,
) : ViewModel() {
    private val _sublistViewState: MutableStateFlow<SublistModel> by lazy {
        val data = MutableStateFlow(SublistModel())
        subredditsRepository.postFetchData.subscribe { newValue ->
            data.value = newValue
        }.also { compositeDisposable.add(it) }
        return@lazy data
    }
    val sublistViewState: StateFlow<SublistModel> = _sublistViewState.asStateFlow()

    init {
        if (_sublistViewState.value.subreddits == null) {
            refreshSublist(ApiSubredditSource.DEFAULT, false)
        }
    }

    fun onSearching(str: String) {
        subredditsRepository.getSubredditsByName(
            isAuth = false,
            query = str,
            limit = 5,
        )
    }

    fun onNavigateBack() {
        navController.navigateUp()
    }

    private fun refreshSublist(source: ApiSubredditSource, isAuth: Boolean) {
        subredditsRepository.fetchSubreddits(source, isAuth)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}