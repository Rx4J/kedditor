package ru.lanik.kedditor.repository

import io.reactivex.rxjava3.subjects.ReplaySubject
import kotlinx.coroutines.flow.StateFlow
import ru.lanik.kedditor.model.SettingsModel

interface SettingsManager {
    interface Reactive {
        val settingsData: ReplaySubject<SettingsModel>
        fun getSettingsModel(): SettingsModel
        fun getStateFlow(): StateFlow<SettingsModel>
        fun updateData(newModel: SettingsModel)
        fun handleError(error: Throwable)
    }
}