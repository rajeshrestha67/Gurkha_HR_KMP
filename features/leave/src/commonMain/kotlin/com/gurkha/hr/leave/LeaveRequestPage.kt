package com.gurkha.hr.leave

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.textField.EPRTextField
import com.gurkha.hr.leave.model.DropDownValue
import com.gurkha.hr.leave.model.LeaveDurationList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestPage() {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    IconButton(
                        onClick = {},
                        content = {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "")

                        }
                    )
                },
                title = {
                    Text(
                        text = stringResource(SharedRes.Strings.leave_request_form),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.darkPrimaryTextColor
                        )
                    )
                }
            )
        },
    ) { paddingValues ->
        LeaveRequestPageContent(modifier = Modifier.fillMaxSize().padding(paddingValues))
    }
}

@Composable
fun LeaveRequestPageContent(modifier: Modifier = Modifier) {
    var selectedLeaveDuration by remember { mutableStateOf<StringResource?>(null) }
    Column(
        modifier = modifier
            .padding(horizontal = MaterialTheme.dimens.small3)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        EPRTextField(
            text = "",
            validateOnFocusChanged = {},
            label = stringResource(SharedRes.Strings.startDate),
            hint = stringResource(SharedRes.Strings.selectStartDate),
            onValueChange = { },
            rules = emptyList(),
            showErrorMessage = false,
            error = null,
            onErrorStateChange = {},
            trailingIcon = {
                Icon(Icons.Filled.CalendarMonth, contentDescription = "")
            }
        )
        EPRTextField(
            text = "",
            label = stringResource(SharedRes.Strings.endDate),
            hint = stringResource(SharedRes.Strings.selectEndDate),
            onValueChange = { },
            rules = emptyList(),
            showErrorMessage = false,
            error = null,
            onErrorStateChange = {},
            trailingIcon = {
                Icon(Icons.Filled.CalendarMonth, contentDescription = "")
            }
        )
        CostumeDropDown(
            label = SharedRes.Strings.leave_duration,
            hint = SharedRes.Strings.select_leave_duration,
            dropDownItems = LeaveDurationList,
            selectedValue = selectedLeaveDuration,
            onValueSelected = { selectedLeaveDuration = it }
        )
    }
}


@Composable
fun CostumeDropDown(
    label: StringResource,
    hint: StringResource,
    dropDownItems: List<DropDownValue>,
    selectedValue: StringResource?,
    onValueSelected: (StringResource) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        EPRTextField(
            modifier = Modifier.clickable { expanded = true },
            text = selectedValue?.let { stringResource(selectedValue) } ?: "",
            label = stringResource(label),
            hint = stringResource(hint),
            onValueChange = { },
            rules = emptyList(),
            showErrorMessage = false,
            error = null,
            onErrorStateChange = {},
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            readOnly = true,
        )

        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dropDownItems.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onValueSelected(item.title)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = stringResource(item.title),
                            textAlign = TextAlign.Start
                        )
                    }
                )
            }
        }
    }
}
