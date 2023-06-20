package ru.lanik.kedditor.ui.screen.sublist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lanik.kedditor.R
import ru.lanik.kedditor.ui.helper.CustomPaddingTextField
import ru.lanik.kedditor.ui.helper.CustomTextFieldColors
import ru.lanik.kedditor.ui.helper.SubredditRow
import ru.lanik.kedditor.ui.theme.KedditorTheme
import ru.lanik.kedditor.ui.theme.SetNavigationBarColor
import ru.lanik.kedditor.ui.theme.SetStatusBarColor

@Composable
fun SublistScreen(
    viewModel: SublistViewModel,
) {
    val searchVal = remember { mutableStateOf("") }
    val viewState by viewModel.sublistViewState.collectAsState()
    SetStatusBarColor(KedditorTheme.colors.secondaryBackground)
    SetNavigationBarColor(KedditorTheme.colors.primaryBackground)
    Surface(
        color = KedditorTheme.colors.primaryBackground,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            TopSublistScreenBar(
                text = searchVal.value,
                isLoading = viewState.subreddits == null,
                onTextChange = {
                    searchVal.value = it
                    viewModel.onSearching(it)
                },
                onBackClicked = viewModel::onNavigateBack,
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn {
                if (searchVal.value.isNotEmpty()) {
                    viewState.subredditSearch?.forEach {
                        item {
                            SubredditRow(
                                subredditName = it.name,
                                subredditSubs = it.subscribers ?: 0,
                                subredditIcon = it.imageUrl,
                                onClick = { },
                            )
                        }
                    }
                } else {
                    viewState.subreddits?.forEach {
                        item {
                            SubredditRow(
                                subredditName = it.name,
                                subredditSubs = it.subscribers ?: 0,
                                subredditIcon = it.imageUrl,
                                onClick = { },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopSublistScreenBar(
    text: String,
    isLoading: Boolean,
    onTextChange: (String) -> Unit = {},
    onBackClicked: () -> Unit = {},
    onMoreClicked: () -> Unit = {},
) {
    Surface(
        shape = KedditorTheme.shapes.cornersStyle,
        color = KedditorTheme.colors.secondaryBackground,
        shadowElevation = 12.dp,
        tonalElevation = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = KedditorTheme.colors.tintColor,
                )
            }
            CustomPaddingTextField(
                value = text,
                placeholderValue = stringResource(id = R.string.sublist_search_placeholder),
                readOnly = isLoading,
                onValueChange = {
                    onTextChange(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                contentPadding = PaddingValues(
                    horizontal = KedditorTheme.shapes.textHorizontalPadding,
                    vertical = KedditorTheme.shapes.textVerticalPadding,
                ),
                colors = CustomTextFieldColors(
                    textColor = KedditorTheme.colors.primaryText,
                    placeholderColor = KedditorTheme.colors.primaryText,
                    cursorColor = KedditorTheme.colors.tintColor,
                ),
                textStyle = KedditorTheme.typography.toolbar,
                modifier = Modifier.weight(1f),
            )

            if (isLoading) {
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    color = KedditorTheme.colors.tintColor,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                IconButton(onClick = { onMoreClicked() }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                        tint = KedditorTheme.colors.tintColor,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SublistScreenPreview() {
    KedditorTheme(
        darkTheme = true,
    ) {
        Surface(
            color = KedditorTheme.colors.primaryBackground,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
            ) {
                TopSublistScreenBar(
                    text = "",
                    isLoading = false,
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn {
                    item {
                        SubredditRow("Test")
                    }
                }
            }
        }
    }
}