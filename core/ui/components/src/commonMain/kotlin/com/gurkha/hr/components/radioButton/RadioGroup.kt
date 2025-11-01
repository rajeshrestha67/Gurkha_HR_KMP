package com.gurkha.hr.components.radioButton

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> RadioGroup(
    modifier: Modifier = Modifier,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T, Boolean) -> Unit,
    title: StringResource? = null,
    orientation: RadioGroupOrientation = RadioGroupOrientation.Vertical()
) {
    Column(
        modifier = modifier
    ) {
        title?.let {
            Text(
                text = stringResource(it),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.erpColors.primaryTextColor
                )
            )
        }
    }
    when (orientation) {
        is RadioGroupOrientation.Vertical -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = orientation.arrangement,
                horizontalAlignment = orientation.alignment
            ) {
                repeat(options.size) { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = option == selectedOption,
                                onClick = { onOptionSelected(options[option]) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = MaterialTheme.dimens.small1)
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = { onOptionSelected(options[option]) }
                        )
                        optionLabel(options[option], option == selectedOption)
                    }
                }
            }
        }

        is RadioGroupOrientation.Horizontal -> {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = orientation.arrangement,
                verticalAlignment = orientation.alignment
            ) {
                items(options) { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = option == selectedOption,
                                onClick = { onOptionSelected(option) },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = { onOptionSelected(option) }
                        )
                        optionLabel(option, option == selectedOption)
                    }
                }
            }
        }
    }
}