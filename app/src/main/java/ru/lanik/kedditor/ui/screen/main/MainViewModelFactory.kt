package ru.lanik.kedditor.ui.screen.main

import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.lanik.kedditor.repository.PostRepository
import ru.lanik.kedditor.repository.SettingsManager
import ru.lanik.kedditor.repository.SubredditsRepository

class MainViewModelFactory(
    private val compositeDisposable: CompositeDisposable,
    private val postRepository: PostRepository.Reactive,
    private val subredditsRepository: SubredditsRepository.Reactive,
) {
    fun getViewModel(
        navController: NavController,
        settingsManager: SettingsManager.Reactive,
    ): MainViewModel {
        return MainViewModel(
            compositeDisposable = compositeDisposable,
            postRepository = postRepository,
            subredditsRepository = subredditsRepository,
            navController = navController,
            settingsManager = settingsManager,
        )
    }
}