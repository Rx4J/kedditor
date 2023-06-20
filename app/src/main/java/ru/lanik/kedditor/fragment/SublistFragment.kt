package ru.lanik.kedditor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lanik.kedditor.ui.screen.sublist.SublistScreen
import ru.lanik.kedditor.ui.screen.sublist.SublistViewModelFactory
import ru.lanik.kedditor.ui.theme.KedditorTheme
import javax.inject.Inject

@AndroidEntryPoint
class SublistFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: SublistViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                KedditorTheme {
                    SublistScreen(
                        viewModel = viewModelFactory.getViewModel(
                            navController = findNavController(),
                        ),
                    )
                }
            }
        }
    }
}