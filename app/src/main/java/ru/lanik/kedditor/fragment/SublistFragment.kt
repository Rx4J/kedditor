package ru.lanik.kedditor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lanik.kedditor.ui.screen.sublist.SublistScreen
import ru.lanik.kedditor.ui.screen.sublist.SublistViewModel
import ru.lanik.kedditor.ui.theme.KedditorTheme

@AndroidEntryPoint
class SublistFragment : Fragment() {
    private val viewModel: SublistViewModel by viewModels()
    private val navController = findNavController()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                KedditorTheme {
                    SublistScreen(
                        viewModel = viewModel,
                        navController = navController,
                    )
                }
            }
        }
    }
}