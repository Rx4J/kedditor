package ru.lanik.kedditor.repository

import androidx.datastore.rxjava3.RxDataStore
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.ReplaySubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.kedditor.model.SettingsModel

@OptIn(ExperimentalCoroutinesApi::class)
class RxSettingsManager(
    private val settingsDataStore: RxDataStore<SettingsModel>,
    private val compositeDisposable: CompositeDisposable,
) : SettingsManager.Reactive {
    override val settingsData: ReplaySubject<SettingsModel> = ReplaySubject.create(1)

    init {
        settingsDataStore.data().subscribe({
            settingsData.onNext(it)
        }, { handleError(it) }).addTo(compositeDisposable)
    }

    override fun updateData(newModel: SettingsModel) {
        settingsDataStore.updateDataAsync { Single.just(newModel) }
    }

    override fun getSettingsModel(): SettingsModel {
        return settingsData.value ?: SettingsModel()
    }

    override fun getStateFlow(): StateFlow<SettingsModel> {
        val settingsViewState: MutableStateFlow<SettingsModel> by lazy {
            val data = MutableStateFlow(SettingsModel())
            settingsData.subscribe({ newValue ->
                data.value = newValue
            }, {
            }).addTo(compositeDisposable)
            return@lazy data
        }
        return settingsViewState.asStateFlow()
    }

    override fun handleError(error: Throwable) {
        settingsData.onError(error)
    }
}