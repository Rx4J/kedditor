package ru.lanik.kedditor.di

import android.content.Context
import androidx.datastore.rxjava3.RxDataStore
import androidx.datastore.rxjava3.RxDataStoreBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.StateFlow
import ru.lanik.kedditor.model.SettingsModel
import ru.lanik.kedditor.model.SettingsModelSerializer
import ru.lanik.kedditor.repository.PostRepository
import ru.lanik.kedditor.repository.RxPostRepository
import ru.lanik.kedditor.repository.RxSettingsManager
import ru.lanik.kedditor.repository.RxSubredditsRepository
import ru.lanik.kedditor.repository.SettingsManager
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.kedditor.ui.screen.main.MainViewModelFactory
import ru.lanik.kedditor.ui.screen.settings.SettingsViewModelFactory
import ru.lanik.kedditor.ui.screen.sublist.SublistViewModelFactory
import ru.lanik.kedditor.utils.NetworkScheduler
import ru.lanik.kedditor.utils.SchedulerPolicy
import ru.lanik.network.api.PostAPI
import ru.lanik.network.api.SubredditsAPI
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideSchedulerPolicy(): SchedulerPolicy = NetworkScheduler()

    @Provides
    @Singleton
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context,
    ): RxDataStore<SettingsModel> = RxDataStoreBuilder(
        context = context,
        fileName = "settings.json",
        serializer = SettingsModelSerializer,
    ).build()

    @Provides
    @Singleton
    fun provideSettingsStateFlow(
        settingsManager: SettingsManager.Reactive,
    ): StateFlow<SettingsModel> = settingsManager.getStateFlow()

    @Provides
    @Singleton
    fun provideRxSubredditsRepository(
        subredditsAPI: SubredditsAPI,
        schedulerPolicy: SchedulerPolicy,
        settingsStateFlow: StateFlow<SettingsModel>,
        compositeDisposable: CompositeDisposable,
    ): SubredditsRepository.Reactive = RxSubredditsRepository(subredditsAPI, schedulerPolicy, settingsStateFlow, compositeDisposable)

    @Provides
    @Singleton
    fun provideRxPostRepository(
        postAPI: PostAPI,
        subredditsAPI: SubredditsAPI,
        schedulerPolicy: SchedulerPolicy,
        settingsStateFlow: StateFlow<SettingsModel>,
        compositeDisposable: CompositeDisposable,
    ): PostRepository.Reactive = RxPostRepository(postAPI, subredditsAPI, schedulerPolicy, settingsStateFlow, compositeDisposable)

    @Provides
    @Singleton
    fun provideRxSettingsManager(
        settingsDataStore: RxDataStore<SettingsModel>,
        compositeDisposable: CompositeDisposable,
    ): SettingsManager.Reactive = RxSettingsManager(settingsDataStore, compositeDisposable)

    @Provides
    @Singleton
    fun provideSublistViewModelFactory(
        compositeDisposable: CompositeDisposable,
        subredditsRepository: SubredditsRepository.Reactive,
    ): SublistViewModelFactory = SublistViewModelFactory(compositeDisposable, subredditsRepository)

    @Provides
    @Singleton
    fun provideMainViewModelFactory(
        compositeDisposable: CompositeDisposable,
        postRepository: PostRepository.Reactive,
        subredditsRepository: SubredditsRepository.Reactive,
    ): MainViewModelFactory = MainViewModelFactory(compositeDisposable, postRepository, subredditsRepository)

    @Provides
    @Singleton
    fun provideSettingsViewModelFactory(
        compositeDisposable: CompositeDisposable,
    ): SettingsViewModelFactory = SettingsViewModelFactory(compositeDisposable)
}