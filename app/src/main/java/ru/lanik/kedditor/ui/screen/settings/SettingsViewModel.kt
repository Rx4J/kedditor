package ru.lanik.kedditor.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.kedditor.model.SettingsModel
import ru.lanik.kedditor.repository.SettingsManager

class SettingsViewModel(
    private val settingsManager: SettingsManager.Reactive,
    private val compositeDisposable: CompositeDisposable,
    private val navController: NavController,
) : ViewModel() {
    private val _settingsViewState: MutableStateFlow<SettingsModel> by lazy {
        val data = MutableStateFlow(SettingsModel())
        settingsManager.settingsData.subscribe({ newValue ->
            data.value = newValue
        }, {
        }).addTo(compositeDisposable)
        return@lazy data
    }
    val settingsViewState: StateFlow<SettingsModel> = _settingsViewState.asStateFlow()

    fun onSettingsChange(newSettingsModel: SettingsModel) {
        settingsManager.updateData(newSettingsModel)
    }

    fun onNavigateBack() {
        navController.navigateUp()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}