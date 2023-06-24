package ru.lanik.kedditor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lanik.kedditor.repository.SettingsManager
import ru.lanik.kedditor.ui.screen.sublist.SublistScreen
import ru.lanik.kedditor.ui.screen.sublist.SublistViewModelFactory
import ru.lanik.kedditor.ui.theme.KedditorTheme
import ru.lanik.kedditor.ui.theme.SetNavigationBarColor
import ru.lanik.kedditor.ui.theme.SetStatusBarColor
import javax.inject.Inject

@AndroidEntryPoint
class SublistFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: SublistViewModelFactory

    @Inject
    lateinit var settingsManager: SettingsManager.Reactive

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val settingsFlow = settingsManager.getStateFlow().collectAsState()
                KedditorTheme(
                    textSize = settingsFlow.value.textSize,
                    paddingSize = settingsFlow.value.paddingSize,
                    corners = settingsFlow.value.cornerStyle,
                    darkTheme = settingsFlow.value.isDark,
                ) {
                    SetStatusBarColor(
                        color = KedditorTheme.colors.secondaryBackground,
                        isDarkMode = settingsFlow.value.isDark,
                    )
                    SetNavigationBarColor(
                        color = KedditorTheme.colors.primaryBackground,
                        isDarkMode = settingsFlow.value.isDark,
                    )
                    SublistScreen(
                        viewModel = viewModelFactory.getViewModel(
                            navController = findNavController(),
                            settingsManager = settingsManager,
                        ),
                        onFragmentResult = {
                            setFragmentResult(
                                "sublist_fragment",
                                bundleOf("subreddit" to it),
                            )
                        },
                    )
                }
            }
        }
    }
}