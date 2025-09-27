package com.gurkha.hr.components.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import com.gurkha.hr.components.noRippleClickable
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.disabledTextFieldBorderColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


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
    error: StringResource? = null,
    maxLength: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    rules: List<Rule> = listOf(),
    onErrorStateChange: (ErrorStatus?) -> Unit,
    enabled: Boolean = true,
    showErrorMessage: Boolean = true,
    height: Dp? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
    shape: Shape = MaterialTheme.shapes.medium,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.borderColor,
    onDropDown: (() -> Unit)? = null
) {

    var hasUserInteracted by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
    ) {
        label?.let {
            Text(
                modifier = Modifier.padding(bottom = MaterialTheme.dimens.small1),
                text = it,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (enabled) MaterialTheme.colorScheme.primaryTextColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor
                )
            )
        }


        val updatedModifier = height?.let {
            Modifier.height(it)
        } ?: Modifier

//        val clickableModifier = onDropDown?.let {
//            Modifier.clickable {
//                if (enabled) {
//                    onDropDown()
//                }
//            }
//        } ?: Modifier
        val clickableModifier = if (enabled && onDropDown != null) {
            Modifier.noRippleClickable {
                onDropDown()
            }
        } else Modifier


        OutlinedTextField(
            enabled = enabled && onDropDown == null,
            modifier = Modifier
                .fillMaxWidth()
                .then(clickableModifier)
                .background(
                    color = backgroundColor,
                    shape = shape
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
                }.then(updatedModifier),
            shape = shape,
            leadingIcon = leadingIcon,
            trailingIcon = {
                trailingIcon?.let {
                    it()
                }
            },
            maxLines = maxLines,
            value = textFieldValue,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
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
                        color = if (enabled) MaterialTheme.colorScheme.primaryTextColor.copy(
                            0.5f
                        ) else MaterialTheme.colorScheme.disabledTextFieldBorderColor
                    ),
                )
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            readOnly = readOnly,
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (enabled && onDropDown == null) focusedBorderColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor,
                unfocusedBorderColor = if (enabled && onDropDown == null) unfocusedBorderColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor,
                disabledBorderColor = if (enabled && onDropDown != null) unfocusedBorderColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor
            )
        )
        AnimatedVisibility(
            visible = error != null && showErrorMessage
        ) {
            error?.let {
                Text(
                    modifier = Modifier.padding(MaterialTheme.dimens.small1),
                    text = stringResource(error),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
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
    error: StringResource? = null,
    maxLength: Int = Int.MAX_VALUE,
    rules: List<Rule> = listOf(),
    onErrorStateChange: (ErrorStatus?) -> Unit,
    enabled: Boolean = true,
    showErrorMessage: Boolean = true,
    height: Dp? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
    shape: Shape = MaterialTheme.shapes.medium,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.borderColor,
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
        backgroundColor = backgroundColor,
        shape = shape,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
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
    error: StringResource? = null,
    maxLength: Int = Int.MAX_VALUE,
    rules: List<Rule>,
    onErrorStateChange: (ErrorStatus?) -> Unit,
    enabled: Boolean = true,
    showErrorMessage: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    height: Dp? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
    shape: Shape = MaterialTheme.shapes.medium,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.borderColor,
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
        backgroundColor = backgroundColor,
        shape = shape,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
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
    error: StringResource? = null,
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
    error: StringResource? = null,
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
    error: StringResource? = null,
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
        readOnly = false,
        error = error,
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
