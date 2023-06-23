package ru.lanik.kedditor.ui.helper

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.lanik.kedditor.R
import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.kedditor.ui.theme.KedditorTheme
import ru.lanik.network.models.Post

@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 2,
    onLoadMore: () -> Unit = {},
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                onLoadMore()
            }
    }
}

@Composable
fun InfinityPostView(
    modifier: Modifier = Modifier,
    backgroundColor: Color = KedditorTheme.colors.primaryBackground,
    posts: List<Post>? = null,
    isNewPath: Boolean = false,
    onLoadMore: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Surface(
        color = backgroundColor,
        modifier = modifier,
    ) {
        LazyColumn(
            state = listState,
        ) {
            posts?.let { notNull ->
                items(notNull) {
                    PostViewItem(
                        post = it,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
    InfiniteListHandler(
        listState = listState,
    ) {
        onLoadMore()
    }

    SideEffect {
        coroutineScope.launch {
            if (isNewPath) {
                listState.scrollToItem(0)
            }
        }
    }
}

@Composable
fun ErrorHandlerView(
    errorState: DefaultError?,
    modifier: Modifier = Modifier,
    loadingState: Boolean = false,
    backgroundColor: Color = KedditorTheme.colors.primaryBackground,
    errorTextColor: Color = KedditorTheme.colors.primaryText,
    content: @Composable () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
    ) {
        errorState?.let {
            if (loadingState && errorState != DefaultError.NO) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (errorState) {
                        DefaultError.NO_INTERNET -> {
                            ErrorViewItem(
                                imageId = R.drawable.where_connect,
                                errorMessage = stringResource(id = R.string.error_no_internet),
                                errorTextColor = errorTextColor,
                            )
                        }
                        DefaultError.UNKNOWN_HOST -> {
                            ErrorViewItem(
                                imageId = R.drawable.where_connect,
                                errorMessage = stringResource(id = R.string.error_unknown_host),
                                errorTextColor = errorTextColor,
                            )
                        }
                        else -> {
                            ErrorViewItem(
                                imageId = R.drawable.where_connect,
                                errorMessage = stringResource(id = R.string.error_other),
                                errorTextColor = errorTextColor,
                            )
                        }
                    }
                }
            } else {
                content()
                when (errorState) {
                    else -> { /* TODO */ }
                }
            }
        }
    }
}

@Composable
fun ErrorViewItem(
    imageId: Int,
    errorMessage: String,
    errorTextColor: Color = KedditorTheme.colors.primaryText,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val gifWidth = ((screenWidth * 3) / 4).dp
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(imageId, imageLoader),
        contentDescription = null,
        modifier = Modifier.width(gifWidth),
    )
    Text(
        text = errorMessage,
        color = errorTextColor,
        style = KedditorTheme.typography.body,
        modifier = Modifier.width(gifWidth),
    )
}