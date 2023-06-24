package ru.lanik.kedditor.ui.screen.sublist

import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.lanik.kedditor.repository.SettingsManager
import ru.lanik.kedditor.repository.SubredditsRepository

class SublistViewModelFactory(
    private val compositeDisposable: CompositeDisposable,
    private val subredditsRepository: SubredditsRepository.Reactive,
) {
    fun getViewModel(
        navController: NavController,
        settingsManager: SettingsManager.Reactive,
    ): SublistViewModel {
        return SublistViewModel(
            compositeDisposable = compositeDisposable,
            subredditsRepository = subredditsRepository,
            navController = navController,
            settingsManager = settingsManager,
        )
    }
}