package ru.lanik.kedditor.ui.helper

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.lanik.kedditor.ui.theme.KedditorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPaddingTextField(
    value: String,
    placeholderValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    colors: CustomTextFieldColors = CustomTextFieldColors.defaultKedditorColors(),
    contentPadding: PaddingValues = PaddingValues(10.dp),
) {
    val textColor = textStyle.color.takeOrElse {
        colors.textColor
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    BasicTextField(
        value = value,
        modifier = modifier,
        readOnly = readOnly,
        onValueChange = onValueChange,
        textStyle = mergedTextStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        cursorBrush = SolidColor(colors.cursorColor),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = placeholderValue,
                        style = KedditorTheme.typography.toolbar,
                        color = colors.placeholderColor,
                    )
                },
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = contentPadding,
            )
        },
    )
}

data class CustomTextFieldColors(
    val textColor: Color,
    val placeholderColor: Color,
    val cursorColor: Color,
) {
    companion object {
        @Composable
        fun defaultKedditorColors(): CustomTextFieldColors {
            return CustomTextFieldColors(
                textColor = KedditorTheme.colors.primaryText,
                placeholderColor = KedditorTheme.colors.secondaryText,
                cursorColor = KedditorTheme.colors.tintColor,
            )
        }
    }
}