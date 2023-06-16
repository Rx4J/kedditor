package ru.lanik.kedditor.ui.theme

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import dagger.hilt.android.internal.managers.ViewComponentManager

enum class KedditorSize {
    Small, Medium, Big,
}

@Composable
private fun getActivityContext(): Activity {
    val view = LocalView.current
    return if (view.context is ViewComponentManager.FragmentContextWrapper) {
        ((view.context as ContextWrapper).baseContext as Activity)
    } else {
        view.context as Activity
    }
}

@Composable
fun SetStatusBarColor(
    color: Color,
) {
    val darkTheme = isSystemInDarkTheme()
    val view = LocalView.current
    val activityContext = getActivityContext()
    if (!view.isInEditMode) {
        SideEffect {
            val window = activityContext.window
            window.statusBarColor = color.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }
}

@Composable
fun SetNavigationBarColor(
    color: Color,
) {
    val darkTheme = isSystemInDarkTheme()
    val view = LocalView.current
    val activityContext = getActivityContext()
    if (!view.isInEditMode) {
        SideEffect {
            val window = activityContext.window
            window.navigationBarColor = color.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightNavigationBars = !darkTheme
        }
    }
}

@Composable
fun KedditorTheme(
    textSize: KedditorSize = KedditorSize.Medium,
    paddingSize: KedditorSize = KedditorSize.Medium,
    corners: KedditorCorners = KedditorCorners.Rounded,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val typography = KedditorTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                KedditorSize.Small -> 24.sp
                KedditorSize.Medium -> 28.sp
                KedditorSize.Big -> 32.sp
            },
            fontWeight = FontWeight.Bold,
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                KedditorSize.Small -> 14.sp
                KedditorSize.Medium -> 16.sp
                KedditorSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal,
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                KedditorSize.Small -> 14.sp
                KedditorSize.Medium -> 16.sp
                KedditorSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium,
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                KedditorSize.Small -> 10.sp
                KedditorSize.Medium -> 12.sp
                KedditorSize.Big -> 14.sp
            },
        ),
    )

    val shapes = KedditorShape(
        generalPadding = when (paddingSize) {
            KedditorSize.Small -> 12.dp
            KedditorSize.Medium -> 16.dp
            KedditorSize.Big -> 20.dp
        },
        textHorizontalPadding = when (paddingSize) {
            KedditorSize.Small -> 8.dp
            KedditorSize.Medium -> 10.dp
            KedditorSize.Big -> 12.dp
        },
        textVerticalPadding = when (paddingSize) {
            KedditorSize.Small -> 2.dp
            KedditorSize.Medium -> 4.dp
            KedditorSize.Big -> 6.dp
        },
        cornersStyle = when (corners) {
            KedditorCorners.Flat -> RoundedCornerShape(0.dp)
            KedditorCorners.Rounded -> RoundedCornerShape(8.dp)
        },
    )
    SetStatusBarColor(colorScheme.primaryBackground)
    SetNavigationBarColor(colorScheme.primaryBackground)
    CompositionLocalProvider(
        LocalKedditorColors provides colorScheme,
        LocalKedditorTypography provides typography,
        LocalKedditorShape provides shapes,
        content = content,
    )
}

object KedditorTheme {
    val colors: KedditorColors
        @Composable
        get() = LocalKedditorColors.current

    val typography: KedditorTypography
        @Composable
        get() = LocalKedditorTypography.current

    val shapes: KedditorShape
        @Composable
        get() = LocalKedditorShape.current
}