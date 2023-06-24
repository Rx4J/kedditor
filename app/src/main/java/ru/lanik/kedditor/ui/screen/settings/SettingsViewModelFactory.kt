package ru.lanik.kedditor.ui.screen.settings

import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.lanik.kedditor.repository.SettingsManager

class SettingsViewModelFactory(
    private val compositeDisposable: CompositeDisposable,
) {
    fun getViewModel(
        navController: NavController,
        settingsManager: SettingsManager.Reactive,
    ): SettingsViewModel {
        return SettingsViewModel(
            compositeDisposable = compositeDisposable,
            navController = navController,
            settingsManager = settingsManager,
        )
    }
}