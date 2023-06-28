package ru.lanik.kedditor.ui.screen.comments

import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.lanik.kedditor.repository.PostRepository

class CommentsViewModelFactory(
    private val postRepository: PostRepository.Reactive,
    private val compositeDisposable: CompositeDisposable,
) {
    fun getViewModel(
        navController: NavController,
        postUrl: String,
    ): CommentsViewModel {
        return CommentsViewModel(
            compositeDisposable = compositeDisposable,
            navController = navController,
            postRepository = postRepository,
            postUrl = postUrl,
        )
    }
}