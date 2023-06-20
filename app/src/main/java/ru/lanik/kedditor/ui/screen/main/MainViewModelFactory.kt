package ru.lanik.kedditor.ui.screen.main

import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.lanik.kedditor.repository.PostRepository
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.network.constants.DefaultPostSort
import ru.lanik.network.constants.DefaultPostSource

class MainViewModelFactory(
    private val compositeDisposable: CompositeDisposable,
    private val postRepository: PostRepository.Reactive,
    private val subredditsRepository: SubredditsRepository.Reactive,
) {
    fun getViewModel(
        navController: NavController,
        sort: DefaultPostSort = DefaultPostSort.HOT,
        source: String = DefaultPostSource.POPULAR.name.lowercase(),
    ): MainViewModel {
        return MainViewModel(
            compositeDisposable = compositeDisposable,
            postRepository = postRepository,
            subredditsRepository = subredditsRepository,
            navController = navController,
            initSort = sort,
            initSource = source,
        )
    }
}