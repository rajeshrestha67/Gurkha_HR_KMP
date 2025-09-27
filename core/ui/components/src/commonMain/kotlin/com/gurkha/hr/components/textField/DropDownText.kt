package com.gurkha.hr.components.textField

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.gurkha.hr.res.theme.disabledTextFieldBorderColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun <T> DropDownText(
    label: StringResource,
    hint: StringResource,
    selectedValue: String,
    error: StringResource?,
    dropdownTextColor: Color = MaterialTheme.colorScheme.primaryTextColor,
    enabled: Boolean = true,
    onError: (ErrorStatus?) -> Unit,
    listOfItems: List<T>,
    rules: List<Rule> = listOf(),
    itemClicked: (T) -> Unit
) {

    var expandedState by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        EPRTextField(
            modifier =
                Modifier
                    .fillMaxWidth(),
            text = selectedValue,
            onValueChange = { value ->

            },
            readOnly = true,
            label = stringResource(label),
            hint = stringResource(
                hint
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "drop down",
                    tint = if (enabled) MaterialTheme.colorScheme.primaryTextColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor
                )
            },
            onErrorStateChange = { err ->
                onError(err)

            },
            enabled = enabled,
            error = error,
            rules = rules,
            onDropDown = {
                expandedState = true
            }
        )


        DropdownMenu(
            modifier = Modifier.fillMaxWidth(0.915f),
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