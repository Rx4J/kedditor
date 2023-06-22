package ru.lanik.kedditor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lanik.kedditor.ui.screen.main.MainScreen
import ru.lanik.kedditor.ui.screen.main.MainViewModel
import ru.lanik.kedditor.ui.screen.main.MainViewModelFactory
import ru.lanik.kedditor.ui.theme.KedditorTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = viewModelFactory.getViewModel(
            navController = findNavController(),
        )
        return ComposeView(requireContext()).apply {
            setContent {
                KedditorTheme {
                    MainScreen(
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("sublist_fragment") { _, bundle ->
            val result = bundle.getString("subreddit")
            viewModel.fetchPosts(
                newSource = result,
            )
        }
    }
}