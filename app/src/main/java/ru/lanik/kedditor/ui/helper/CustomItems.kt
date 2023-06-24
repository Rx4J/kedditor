package ru.lanik.kedditor.ui.helper

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import ru.lanik.kedditor.R
import ru.lanik.kedditor.model.MenuItemModel
import ru.lanik.kedditor.ui.theme.KedditorTheme
import ru.lanik.kedditor.utils.extension.toFormatStr
import ru.lanik.network.models.Post

@Composable
fun SubredditRow(
    subredditName: String,
    modifier: Modifier = Modifier,
    subredditSubs: Int = 0,
    subredditIcon: String? = null,
    backgroundColor: Color = KedditorTheme.colors.primaryBackground,
    onClick: (String) -> Unit = {},
) {
    val defaultIconUnit = @Composable {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.reddit_icon),
            contentDescription = null,
        )
    }

    Button(
        onClick = { onClick(subredditName) },
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth(),
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(subredditIcon)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    defaultIconUnit()
                },
                error = {
                    defaultIconUnit()
                },
                modifier = modifier
                    .size(32.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = modifier.width(16.dp))
            Column {
                Text(
                    text = subredditName,
                    color = KedditorTheme.colors.primaryText,
                    style = KedditorTheme.typography.body,
                )
                Text(
                    text = "${subredditSubs.toFormatStr()} members",
                    color = KedditorTheme.colors.secondaryText,
                    style = KedditorTheme.typography.caption,
                )
            }
        }
    }
}

@Composable
fun PostViewItem(
    post: Post,
    onDirUp: (String) -> Unit = {},
    onDirDown: (String) -> Unit = {},
) {
    val iconState = post.iconUrl.collectAsState()
    val defaultIconUnit = @Composable {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.reddit_icon),
            contentDescription = null,
        )
    }
    Surface(
        shape = KedditorTheme.shapes.cornersStyle,
        color = KedditorTheme.colors.secondaryBackground,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(KedditorTheme.shapes.generalPadding),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(iconState.value)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = {
                        defaultIconUnit()
                    },
                    error = {
                        defaultIconUnit()
                    },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = post.name,
                    color = KedditorTheme.colors.primaryText,
                    style = KedditorTheme.typography.body,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = post.author,
                    color = KedditorTheme.colors.secondaryText,
                    style = KedditorTheme.typography.caption,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.title,
                color = KedditorTheme.colors.primaryText,
                style = KedditorTheme.typography.heading,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${post.numComments} comments",
                    color = KedditorTheme.colors.secondaryText,
                    style = KedditorTheme.typography.caption,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = post.getDate(),
                    color = KedditorTheme.colors.secondaryText,
                    style = KedditorTheme.typography.caption,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(
                    onClick = { onDirUp(post.url) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = KedditorTheme.colors.tintColor,
                        modifier = Modifier.rotate(90f),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = post.score.toFormatStr("."),
                        color = KedditorTheme.colors.secondaryText,
                        style = KedditorTheme.typography.toolbar,
                    )
                }
                IconButton(
                    onClick = { onDirDown(post.url) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = KedditorTheme.colors.tintColor,
                        modifier = Modifier.rotate(270f),
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    model: MenuItemModel,
    onItemSelected: (Int) -> Unit = {},
) {
    val isDropdownOpen = remember { mutableStateOf(false) }
    val currentPosition = remember { mutableStateOf(model.currentIndex) }

    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier
                    .clickable {
                        isDropdownOpen.value = true
                    }
                    .padding(KedditorTheme.shapes.generalPadding)
                    .background(KedditorTheme.colors.primaryBackground),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = KedditorTheme.shapes.generalPadding),
                    text = model.title,
                    style = KedditorTheme.typography.body,
                    color = KedditorTheme.colors.primaryText,
                )

                Text(
                    text = model.values[currentPosition.value],
                    style = KedditorTheme.typography.body,
                    color = KedditorTheme.colors.secondaryText,
                )

                Icon(
                    modifier = Modifier
                        .padding(start = KedditorTheme.shapes.generalPadding / 4)
                        .size(18.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_baseline_arrow),
                    contentDescription = "Arrow",
                    tint = KedditorTheme.colors.secondaryText,
                )
            }

            Divider(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.BottomStart),
                thickness = 0.5.dp,
                color = KedditorTheme.colors.secondaryText.copy(
                    alpha = 0.3f,
                ),
            )
        }
        DropdownMenu(
            expanded = isDropdownOpen.value,
            onDismissRequest = {
                isDropdownOpen.value = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(KedditorTheme.colors.primaryBackground),
        ) {
            model.values.forEachIndexed { index, value ->
                DropdownMenuItem(
                    onClick = {
                        currentPosition.value = index
                        isDropdownOpen.value = false
                        onItemSelected(index)
                    },
                    text = {
                        Text(
                            text = value,
                            style = KedditorTheme.typography.body,
                            color = KedditorTheme.colors.primaryText,
                        )
                    },
                )
            }
        }
    }
}