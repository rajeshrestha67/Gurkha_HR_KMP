package com.gurkha.hr.addNoteScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ERPDateTextField
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.ERPTimeTestField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.model.addNotes.AddNotesAction
import com.gurkha.hr.model.addNotes.AddNotesState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    onBackClicked: () -> Unit
) {
    val viewModel: AddNotesViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.Blue),
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(text = stringResource(SharedRes.Strings.add_notes))
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    )
                }
            )
        }
    ) { contentPadding ->
        AddNoteScreenContent(
            modifier = Modifier
                .padding(contentPadding),
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
fun AddNoteScreenContent(
    modifier: Modifier = Modifier,
    state: AddNotesState,
    onAction: (AddNotesAction) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState())
            .fillMaxSize()
            .padding(
                start = MaterialTheme.dimens.small3,
                end = MaterialTheme.dimens.small3,
                bottom = MaterialTheme.dimens.bottomBar
            )
    ) {
        StepsBoxRow(
            isEvent = state.isEvent,
            onAction = onAction,
            state = state
        )

        when (state.selectedHeaderTab) {
            1 -> {
                NoteFormField(
                    state = state,
                    onAction = onAction,
                    onToggle = {
                        onAction(AddNotesAction.OnToggleEvent)
                    },
                    isEvent = state.isEvent
                )
            }

            2 -> {
                EventFormField(
                    state = state,
                    onAction = onAction
                )
            }
        }

        ERPButton(
            modifier = Modifier
                .fillMaxWidth().padding(top = MaterialTheme.dimens.medium1),
            text = stringResource(SharedRes.Strings.add_notes),
            onClick = {
                onAction(AddNotesAction.OnSubmit)
            }
        )
    }

}

@Composable
fun StepsBoxRow(
    isEvent: Boolean,
    onAction: (AddNotesAction) -> Unit,
    state: AddNotesState
) {
    //        show steps
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepsBox(
            count = 1,
            text = stringResource(SharedRes.Strings.note),
            onClick = {
                onAction(AddNotesAction.OnHeaderTabChange(1))
            },
            state = state
        )
//            is event is true
        AnimatedVisibility(
            visible = isEvent,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.dimens.small3)
                        .weight(2f),
                    thickness = MaterialTheme.dimens.extraSmall
                )
                StepsBox(
                    count = 2,
                    text = stringResource(SharedRes.Strings.event),
                    onClick = {
                        onAction(AddNotesAction.OnHeaderTabChange(2))
                    },
                    state = state
                )
            }
        }

    }
}


@Composable
fun StepsBox(
    count: Int,
    text: String,
    onClick: () -> Unit,
    state: AddNotesState
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val color =
            if (state.selectedHeaderTab == count) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.borderColor
        Box(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .clickable(
                    onClick = onClick
                )
                .size(45.dp)
                .border(
                    width = 2.dp, shape = RoundedCornerShape(100),
                    color = color,
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(), style = MaterialTheme.typography.titleLarge.copy(
                    color = color
                )
            )
        }
        Text(
            text = text, style = MaterialTheme.typography.bodyMedium.copy(
                color = color
            )
        )
    }
}


@Composable
fun NoteFormField(
    onAction: (AddNotesAction) -> Unit,
    state: AddNotesState,
    onToggle: () -> Unit,
    isEvent: Boolean
) {
    Column(
        modifier = Modifier.padding(vertical = MaterialTheme.dimens.small3),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        ERPTextField(
            text = state.title,
            label = stringResource(SharedRes.Strings.title),
            hint = stringResource(SharedRes.Strings.enter_title),
            error = state.titleError,
            onValueChange = {
                onAction(AddNotesAction.OnTitleChange(it))
            },
            rules = FormValidate.requiredValidationRules,
            onErrorStateChange = {},
            enabled = true,
            showErrorMessage = true
        )

        ERPTextField(
            text = state.description,
            label = stringResource(SharedRes.Strings.reason),
            hint = stringResource(SharedRes.Strings.enterReason),
            onValueChange = {
                onAction(AddNotesAction.OnDescriptionChange(it))
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            rules = FormValidate.requiredValidationRules,
            error = state.descriptionError,
            onErrorStateChange = {},
            keyboardActions = KeyboardActions(
                onSend = {
//                    onAction(LeaveRequestScreenAction.Submit)
                }
            ),
            height = MaterialTheme.dimens.chartHeight
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = isEvent,
                onCheckedChange = {
                    onToggle()
                },
                thumbContent = {
                    if (isEvent) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = ""
                        )
                    }
                }
            )
            Text(
                text = stringResource(SharedRes.Strings.is_event),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
        }
    }
}

@Composable
fun EventFormField(
    state: AddNotesState,
    onAction: (AddNotesAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = MaterialTheme.dimens.small3),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        ERPDateTextField(
            label = stringResource(SharedRes.Strings.startDate),
            hint = stringResource(SharedRes.Strings.startDate),
            value = state.startDate,
            error = state.startDateError,
            rules = FormValidate.requiredValidationRules,
            onErrorStateChange = {},
            enabled = true,
            onDateSelected = {
                onAction(AddNotesAction.OnStartDateChange(it))
            }
        )

        ERPDateTextField(
            label = stringResource(SharedRes.Strings.endDate),
            hint = stringResource(SharedRes.Strings.endDate),
            value = state.endDate,
            error = state.endDateError,
            rules = FormValidate.requiredValidationRules,
            onErrorStateChange = {},
            enabled = true,
            onDateSelected = {
                onAction(AddNotesAction.OnEndDateChange(it))
            }
        )

        ERPTimeTestField(
            label = stringResource(SharedRes.Strings.start_time),
            hint = stringResource(SharedRes.Strings.start_time),
            value = state.startTime,
            error = state.startTimeError,
            rules = FormValidate.requiredValidationRules,
            onErrorStateChange = {},
            enabled = true,
            onTimeSelected = {
                onAction(AddNotesAction.OnStartTimeChange(it))
            }
        )

        ERPTimeTestField(
            label = stringResource(SharedRes.Strings.end_time),
            hint = stringResource(SharedRes.Strings.end_time),
            value = state.endTime,
            error = state.endTimeError,
            rules = FormValidate.requiredValidationRules,
            onErrorStateChange = {},
            enabled = true,
            onTimeSelected = {
                onAction(AddNotesAction.OnEndTimeChange(it))
            }
        )

        ERPTextField(
            label = stringResource(SharedRes.Strings.location),
            hint = stringResource(SharedRes.Strings.location),
            text = state.location,
            error = state.locationError,
            rules = FormValidate.requiredValidationRules,
            onErrorStateChange = {
            },
            enabled = true,
            onValueChange = {
                onAction(AddNotesAction.OnLocationChange(it))
            }
        )


    }
}
