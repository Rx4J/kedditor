package ru.lanik.kedditor.model

import kotlinx.serialization.Serializable
import ru.lanik.kedditor.ui.theme.KedditorCorners
import ru.lanik.kedditor.ui.theme.KedditorSize
import ru.lanik.network.constants.DefaultPostSort
import ru.lanik.network.constants.DefaultPostSource
import ru.lanik.network.constants.DefaultSubredditSource

@Serializable
data class SettingsModel(
    val isDark: Boolean = true,
    val isAuth: Boolean = false,
    val textSize: KedditorSize = KedditorSize.Medium,
    val paddingSize: KedditorSize = KedditorSize.Medium,
    val cornerStyle: KedditorCorners = KedditorCorners.Rounded,
    val defaultPostSource: DefaultPostSource = DefaultPostSource.POPULAR,
    val defaultPostSort: DefaultPostSort = DefaultPostSort.HOT,
    val defaultSubredditSource: DefaultSubredditSource = DefaultSubredditSource.DEFAULT,
)
