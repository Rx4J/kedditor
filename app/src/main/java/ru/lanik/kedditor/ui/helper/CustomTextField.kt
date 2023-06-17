package ru.lanik.kedditor.ui.helper

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomPaddingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    placeholder: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    contentPadding: PaddingValues = PaddingValues(10.dp),
) {
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(true).value
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
        cursorBrush = SolidColor(colors.cursorColor(false).value),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = placeholder,
                colors = colors,
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = contentPadding,
            )
        },
    )
}