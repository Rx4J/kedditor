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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lanik.kedditor.R
import ru.lanik.kedditor.ui.helper.CustomPaddingTextField

@Composable
fun SublistScreen() {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            TopSublistScreenBar(
                text = "",
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                item {
                    SubredditRow("Test")
                }
            }
        }
    }
}

@Composable
fun TopSublistScreenBar(
    text: String,
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
                onValueChange = {
                    onTextChange(it)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.sublist_search_placeholder))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.None,
                ),
                contentPadding = PaddingValues(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                modifier = Modifier.weight(1f),
            )
            
            IconButton(onClick = { onMoreClicked() }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun SubredditRow(
    subredditName: String,
    subredditIcon: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.reddit_icon),
    onClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClick()
        },
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            bitmap = subredditIcon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = subredditName)
    }
}

@Preview
@Composable
fun SublistScreenPreview() {
    SublistScreen()
}