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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.launch
import ru.lanik.kedditor.R
import ru.lanik.kedditor.constants.DefaultError
import ru.lanik.kedditor.ui.theme.KedditorTheme
import ru.lanik.network.models.Post

@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    listSize: Int,
    onLoadMore: () -> Unit = {},
) {
    val loadMore = remember {
        derivedStateOf {
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val visibleItemsCount = listState.layoutInfo.visibleItemsInfo.size
            val percent = (firstVisibleItemIndex / (listSize - visibleItemsCount).toFloat()) * 100f
            listSize > 0 && percent > 99f
        }
    }

    if (loadMore.value) {
        onLoadMore()
    }
}

@Composable
fun InfinityPostView(
    modifier: Modifier = Modifier,
    onPostClick: (String) -> Unit = {},
    backgroundColor: Color = KedditorTheme.colors.primaryBackground,
    posts: List<Post>? = null,
    onLoadMore: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                coroutineScope.launch {
                    listState.scrollToItem(0)
                }
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

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
                        onPostClick = onPostClick,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
    InfiniteListHandler(
        listState = listState,
        listSize = posts?.size ?: 0,
    ) {
        onLoadMore()
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
        textAlign = TextAlign.Center,
    )
}