package com.gurkha.hr.components.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun EPRBaseTextField(
    modifier: Modifier = Modifier,
    textFieldValue: TextFieldValue,
    label: String?,
    hint: String,
    onValueChange: (TextFieldValue) -> Unit,
    validateOnFocusChanged: (FocusState) -> Unit,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    error: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    rules: List<Rule> = listOf(),
    onErrorStateChange: (ErrorStatus?) -> Unit,
    enabled: Boolean = true,
    showErrorMessage: Boolean = true,
    height: Dp? = null,
    bgColor: Color = Color.Green.copy(alpha = 0.1f),
    bgShape: Shape = MaterialTheme.shapes.medium,
    borderEnabled: Boolean = true,
    onDropDown: (() -> Unit)? = null
) {

    var hasUserInteracted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        label?.let {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = it,
                style = MaterialTheme.typography.bodySmall
//                fontWeight = FontWeight.W400,
//                color = MaterialTheme.agColors.defaultTextDarkColor.copy(alpha = if (enabled) 1f else 0.5f)
            )

        }


        val updatedModifier = height?.let {
            Modifier.height(it)
        } ?: Modifier

        val clickableModifier = onDropDown?.let {
            Modifier.clickable {
                onDropDown()
            }
        } ?: Modifier

        println("onDropDown $onDropDown")
        OutlinedTextField(
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    bgColor, bgShape
                )
                .onFocusChanged { focusState ->
                    if (hasUserInteracted) {
                        if (!focusState.isFocused) {
                            // Validate when focus is lost
                            validateOnFocusChanged(focusState)
                            onErrorStateChange(
                                rules.validate(text = textFieldValue.text)
                            )
                        }
                    } else {
                        if (focusState.isFocused) {
                            hasUserInteracted = true
                        }
                    }
                }.then(updatedModifier).then(clickableModifier),
            shape = bgShape,
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (error != null) {
//                    Image(
//                        painter = painterResource(R.drawable.ic_input_field_error),
//                        contentDescription = error
//                    )
                } else {
                    trailingIcon?.let {
                        it()
                    }
                }
            },
            maxLines = maxLines,
            value = textFieldValue,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = Color.Black
            ),
            onValueChange = {
                if (it.text.length <= maxLength) {
                    onValueChange(it)
                }
            },
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    ),
                )
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            readOnly = readOnly,
            isError = error != null,
            colors = if (borderEnabled) {
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Green.copy(alpha = if (enabled) 1f else 0.5f),
                    unfocusedBorderColor = Color.Gray.copy(alpha = if (enabled) 1f else 0.5f),
                )
            } else OutlinedTextFieldDefaults.colors(
                focusedBorderColor = bgColor,
                unfocusedBorderColor = bgColor
            ),
        )
        AnimatedVisibility(
            visible = error != null && showErrorMessage
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = error ?: "",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Red
                )
            )
        }
    }

}


@Composable
fun EPRTextField(
    modifier: Modifier = Modifier,
    textFieldValue: TextFieldValue,
    label: String? = null,
    hint: String,
    onValueChange: (TextFieldValue) -> Unit,
    validateOnFocusChanged: (FocusState) -> Unit = {},
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    readOnly: Boolean = false,
    error: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    rules: List<Rule> = listOf(),
    onErrorStateChange: (ErrorStatus?) -> Unit,
    enabled: Boolean = true,
    showErrorMessage: Boolean = true,
    height: Dp? = null,
    bgColor: Color = Color.Green.copy(alpha = 0.1f),
    bgShape: Shape = MaterialTheme.shapes.medium,
    borderEnabled: Boolean = true,
    onDropDown: (() -> Unit)? = null
) {
    EPRBaseTextField(
        modifier = modifier,
        textFieldValue = textFieldValue,
        label = label,
        hint = hint,
        onValueChange = onValueChange,
        validateOnFocusChanged = validateOnFocusChanged,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        error = error,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        maxLength = maxLength,
        rules = rules,
        onErrorStateChange = onErrorStateChange,
        enabled = enabled,
        showErrorMessage = showErrorMessage,
        maxLines = maxLines,
        singleLine = singleLine,
        height = height,
        bgColor = bgColor,
        bgShape = bgShape,
        borderEnabled = borderEnabled,
        onDropDown = onDropDown
    )
}


@Composable
fun EPRTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String? = null,
    hint: String,
    onValueChange: (String) -> Unit,
    validateOnFocusChanged: (FocusState) -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    error: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    rules: List<Rule>,
    onErrorStateChange: (ErrorStatus?) -> Unit,
    enabled: Boolean = true,
    showErrorMessage: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    height: Dp? = null,
    bgColor: Color = Color.Green.copy(alpha = 0.1f),
    bgShape: Shape = MaterialTheme.shapes.medium,
    borderEnabled: Boolean = true,
    onDropDown: (() -> Unit)? = null
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = text)) }

    val textFieldValue = textFieldValueState.copy(text = text)

    EPRTextField(
        modifier = modifier,
        textFieldValue = textFieldValue,
        label = label,
        hint = hint,
        onValueChange = {
            textFieldValueState = it
            onValueChange(it.text)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        validateOnFocusChanged = validateOnFocusChanged,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        error = error,
        maxLength = maxLength,
        rules = rules,
        onErrorStateChange = onErrorStateChange,
        showErrorMessage = showErrorMessage,
        enabled = enabled,
        singleLine = singleLine,
        maxLines = maxLines,
        height = height,
        bgColor = bgColor,
        bgShape = bgShape,
        borderEnabled = borderEnabled,
        onDropDown = onDropDown
    )
}

@Composable
fun AGMobileTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "Mobile",
    hint: String = "Hint",
    onValueChange: (String) -> Unit,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    error: String? = null,
    onErrorStateChange: (ErrorStatus?) -> Unit,
    maxLength: Int = 10,
    rules: List<Rule>,
    showErrorMessage: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    height: Dp? = null
) {
    EPRTextField(
        modifier = modifier,
        text = value,
        label = label,
        hint = hint,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        error = error,
        maxLength = maxLength,
        rules = rules,
        onErrorStateChange = onErrorStateChange,
        enabled = enabled,
        showErrorMessage = showErrorMessage,
        maxLines = maxLines,
        singleLine = singleLine,
        height = height
    )
}

@Composable
fun AGEmailTextField(
    modifier: Modifier = Modifier,
    label: String = "Email",
    hint: String = "Enter your email",
    onValueChange: (String) -> Unit,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    error: String? = null,
    onErrorStateChange: (ErrorStatus?) -> Unit,
    maxLength: Int = 320,
    rules: List<Rule>,
    enabled: Boolean = true,
    showErrorMessage: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    height: Dp? = null
) {
    EPRTextField(
        modifier = modifier,
        text = value,
        label = label,
        hint = hint,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        error = error,
        maxLength = maxLength,
        rules = rules,
        onErrorStateChange = onErrorStateChange,
        enabled = enabled,
        showErrorMessage = showErrorMessage,
        singleLine = singleLine,
        maxLines = maxLines,
        height = height
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    label: String = "Email",
    hint: String = "Enter your email",
    onValueChange: (String) -> Unit,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    error: String? = null,
    onErrorStateChange: (ErrorStatus?) -> Unit,
    rules: List<Rule>,
    enabled: Boolean = true,
    showErrorMessage: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = 1
) {
    var revealed by remember { mutableStateOf(false) }

    EPRTextField(
        modifier = modifier,
        text = value,
        label = label,
        hint = hint,
        onValueChange = onValueChange,
        visualTransformation = if (revealed) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        keyboardActions = keyboardActions,
        readOnly = true,
        error = error,
        maxLength = 1,
        rules = rules,
        onErrorStateChange = onErrorStateChange,
        enabled = enabled,
        showErrorMessage = showErrorMessage,
        singleLine = singleLine,
        maxLines = maxLines,
        trailingIcon = {
            val icon = if (revealed) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            val desc = if (revealed) "Hide password" else "Show password"
            IconButton(onClick = { revealed = !revealed }) {
                Icon(imageVector = icon, contentDescription = desc)
            }
        }
    )

}
