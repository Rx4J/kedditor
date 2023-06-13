package ru.lanik.kedditor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.lanik.kedditor.repository.RxSubredditsRepository
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.kedditor.utils.NetworkScheduler
import ru.lanik.kedditor.utils.SchedulerPolicy
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
    fun proviveRxSubredditsRepository(
        subredditsAPI: SubredditsAPI,
        schedulerPolicy: SchedulerPolicy,
        compositeDisposable: CompositeDisposable,
    ): SubredditsRepository.Reactive = RxSubredditsRepository(subredditsAPI, schedulerPolicy, compositeDisposable)
}