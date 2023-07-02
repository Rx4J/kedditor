package ru.lanik.kedditor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lanik.kedditor.repository.SettingsManager
import ru.lanik.kedditor.ui.screen.main.MainScreen
import ru.lanik.kedditor.ui.screen.main.MainViewModel
import ru.lanik.kedditor.ui.screen.main.MainViewModelFactory
import ru.lanik.kedditor.ui.theme.KedditorTheme
import ru.lanik.kedditor.ui.theme.SetNavigationBarColor
import ru.lanik.kedditor.ui.theme.SetStatusBarColor
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    @Inject
    lateinit var settingsManager: SettingsManager.Reactive

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = viewModelFactory.getViewModel(
            navController = findNavController(),
            settingsManager = settingsManager,
        )
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
                        color = KedditorTheme.colors.primaryBackground,
                        isDarkMode = settingsFlow.value.isDark,
                    )
                    SetNavigationBarColor(
                        color = KedditorTheme.colors.primaryBackground,
                        isDarkMode = settingsFlow.value.isDark,
                    )
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
            if ((result?.length ?: 0) > 1) {
                viewModel.setSource(
                    newSource = result!!,
                )
            }
        }
    }
}