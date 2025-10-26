package com.gurkha.hr.components.textField

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.gurkha.hr.components.dimens
import com.gurkha.hr.res.theme.disabledTextFieldBorderColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownText(
    label: StringResource,
    hint: StringResource,
    selectedValue: String,
    error: StringResource?,
    dropdownTextColor: Color = MaterialTheme.colorScheme.primaryTextColor,
    enabled: Boolean = true,
    onError: (StringResource?) -> Unit,
    listOfItems: List<T>,
    rules: List<Rule> = listOf(),
    itemClicked: (T) -> Unit,
    isFetching: Boolean = false,
    isFetchingError: Boolean = false,
    dropdownIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "drop down",
            tint = if (enabled) MaterialTheme.colorScheme.primaryTextColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor

        )
    },
    onRetry: (() -> Unit)? = null
) {

    var expandedState by remember { mutableStateOf(false) }
    var textFieldWidth by remember { mutableStateOf(0) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = "Arrow Rotation"
    )
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        ERPTextField(
            modifier = Modifier

                .onGloballyPositioned { coordinates ->
                    textFieldWidth = coordinates.size.width
                },
            text = selectedValue,
            onValueChange = { value ->

            },
            readOnly = true,
            label = stringResource(label),
            hint = stringResource(
                hint
            ),
            trailingIcon = {
                if (isFetchingError) {
                    IconButton(
                        onClick = {
                            onRetry?.invoke()
                        },
                        modifier = Modifier.size(MaterialTheme.dimens.medium1),
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Replay,
                                contentDescription = "retry",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
//
                    )
                } else if (isFetching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(MaterialTheme.dimens.medium1),
                        strokeWidth = MaterialTheme.dimens.extraSmall
                    )
                } else {
//                    Icon(
//                        modifier = Modifier.rotate(rotationAngle),
//                        imageVector = Icons.Filled.ArrowDropDown,
//                        contentDescription = "drop down",
//                        tint = if (enabled) MaterialTheme.colorScheme.primaryTextColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor
//                    )
                    dropdownIcon(

                    )
                }

            },
            onErrorStateChange = { err ->
                onError(err)
            },
            enabled = enabled,
            error = error,
            rules = rules,
            onDropDown = {
                if (!isFetchingError && !isFetching) expandedState = true else false
            }
        )


        DropdownMenu(
            modifier = Modifier.width(with(LocalDensity.current) { textFieldWidth.toDp() }),
            expanded = expandedState,
            onDismissRequest = { expandedState = false }
        ) {
            listOfItems.forEachIndexed { index, state ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = state.toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = dropdownTextColor
                            )
                        )
                    },
                    onClick = {
                        itemClicked(state)
                        expandedState = false
                    }
                )
                if (index < listOfItems.size - 1) {
                    HorizontalDivider()
                }
            }

        }
    }
}