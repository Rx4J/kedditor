package ru.lanik.kedditor.ui.helper

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
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
    surfaceColor: Color,
    modifier: Modifier = Modifier,
    posts: SnapshotStateList<Post>? = null,
    isNewPath: Boolean = false,
    onLoadMore: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Surface(
        color = surfaceColor,
        modifier = modifier,
    ) {
        LazyColumn(
            state = listState,
        ) {
            posts?.let { notNull ->
                items(notNull) {
                    PostViewItem(post = it)
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