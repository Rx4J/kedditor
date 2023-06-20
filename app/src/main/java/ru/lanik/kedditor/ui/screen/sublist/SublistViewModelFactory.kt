package ru.lanik.kedditor.ui.screen.sublist

import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.network.constants.DefaultSubredditSource

class SublistViewModelFactory(
    private val compositeDisposable: CompositeDisposable,
    private val subredditsRepository: SubredditsRepository.Reactive,
) {
    fun getViewModel(
        navController: NavController,
        source: String = DefaultSubredditSource.DEFAULT.name.lowercase(),
    ): SublistViewModel {
        return SublistViewModel(
            compositeDisposable = compositeDisposable,
            subredditsRepository = subredditsRepository,
            navController = navController,
            initSource = source,
        )
    }
}