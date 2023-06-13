package ru.lanik.kedditor.ui.screen.sublist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.lanik.kedditor.R
import ru.lanik.kedditor.ui.helper.CustomPaddingTextField
import ru.lanik.kedditor.utils.extension.toFormatStr

@Composable
fun SublistScreen(
    viewModel: SublistViewModel,
) {
    val searchVal = remember { mutableStateOf("") }
    val viewState by viewModel.sublistViewState.collectAsState()
    Surface {
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
                onBackClicked = viewModel::onBackClicked,
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
                                onClick = viewModel::onDirectClicked,
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
                                onClick = viewModel::onDirectClicked,
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
                )
            }

            CustomPaddingTextField(
                value = text,
                readOnly = isLoading,
                onValueChange = {
                    onTextChange(it)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.sublist_search_placeholder))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                contentPadding = PaddingValues(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                modifier = Modifier.weight(1f),
            )

            if (isLoading) {
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                IconButton(onClick = { onMoreClicked() }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun SubredditRow(
    subredditName: String,
    subredditSubs: Int = 0,
    subredditIcon: String? = null,
    onClick: (String) -> Unit = {},
) {
    Column {
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    onClick(subredditName)
                }
                .fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            if (subredditIcon == null) {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.reddit_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(subredditIcon)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = subredditName)
                Text(text = "${subredditSubs.toFormatStr()} members", fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Preview
@Composable
fun SublistScreenPreview() {
    Surface {
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