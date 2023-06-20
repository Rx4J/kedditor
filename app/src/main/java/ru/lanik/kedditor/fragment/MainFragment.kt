package ru.lanik.kedditor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lanik.kedditor.ui.screen.main.MainScreen
import ru.lanik.kedditor.ui.screen.main.MainViewModelFactory
import ru.lanik.kedditor.ui.theme.KedditorTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                KedditorTheme {
                    MainScreen(
                        viewModel = viewModelFactory.getViewModel(
                            navController = findNavController(),
                        ),
                    )
                }
            }
        }
    }
}