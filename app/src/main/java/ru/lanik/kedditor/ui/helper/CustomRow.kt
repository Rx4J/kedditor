package ru.lanik.kedditor.ui.helper

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import ru.lanik.kedditor.R
import ru.lanik.kedditor.ui.theme.KedditorTheme
import ru.lanik.kedditor.utils.extension.toFormatStr

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
            backgroundColor = backgroundColor,
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