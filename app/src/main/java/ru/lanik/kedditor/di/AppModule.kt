package ru.lanik.kedditor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.lanik.kedditor.repository.PostRepository
import ru.lanik.kedditor.repository.RxPostRepository
import ru.lanik.kedditor.repository.RxSubredditsRepository
import ru.lanik.kedditor.repository.SubredditsRepository
import ru.lanik.kedditor.ui.screen.main.MainViewModelFactory
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
    fun provideRxSubredditsRepository(
        subredditsAPI: SubredditsAPI,
        schedulerPolicy: SchedulerPolicy,
        compositeDisposable: CompositeDisposable,
    ): SubredditsRepository.Reactive = RxSubredditsRepository(subredditsAPI, schedulerPolicy, compositeDisposable)

    @Provides
    @Singleton
    fun provideRxPostRepository(
        postAPI: PostAPI,
        schedulerPolicy: SchedulerPolicy,
        compositeDisposable: CompositeDisposable,
    ): PostRepository.Reactive = RxPostRepository(postAPI, schedulerPolicy, compositeDisposable)

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
}