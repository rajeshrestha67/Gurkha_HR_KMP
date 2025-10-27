package com.gurkha.hr.profile.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ERPDateTextField
import com.gurkha.hr.components.date.FutureAndTodayDate
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.domain.userDetail.ui.EditProfileUI
import com.gurkha.hr.profile.model.edit_profile_screen.EditProfileScreenState
import com.gurkha.hr.profile.model.edit_profile_screen.EditProfileViewAction
import com.gurkha.hr.profile.model.edit_profile_screen.Title
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.SharedRes.Strings.labelContract
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.veryLightGray
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreen(
    onBackPressed: () -> Unit,
) {
    val viewModel: EditProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditProfileScreenContainer(
        onBackPressed = onBackPressed,
        state = state,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenContainer(
    onBackPressed: () -> Unit,
    onAction: (EditProfileViewAction) -> Unit,
    state: EditProfileScreenState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),

        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(SharedRes.Strings.editProfile)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        EditProfileScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
fun EditProfileScreenContent(
    modifier: Modifier = Modifier,
    state: EditProfileScreenState,
    onAction: (EditProfileViewAction) -> Unit
) {
    val headingList = Title.list.map { stringResource(it.title) }
    Column(
        modifier = modifier
            .padding(
                vertical = MaterialTheme.dimens.small2,
                horizontal = MaterialTheme.dimens.small3
            )
    ) {
        EditProfileTabRow(
            selectedIndex = state.selectedTab,
            items = headingList,
            onTabSelected = { index ->
                onAction(EditProfileViewAction.OnItemSelected(index))
            }

        )

        when (state.selectedTab) {
            0 -> {

                state.profileSummaryList?.let {
                    PersonalDetailsContent(
                        item = state.profileSummaryList,
                        onAction = onAction,
                        state = state
                    )
                }
            }

            1 -> {
                state.profileSummaryList?.let {
                    OthersDetailContent(
                        item = state.profileSummaryList,
                        onAction = onAction,
                        state = state
                    )
                }
            }
        }


    }
}

@Composable
fun SubmitButton(
    onAction: () -> Unit,
    isSubmit: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MaterialTheme.dimens.small3),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
    ) {
        val text = if(isSubmit)stringResource(SharedRes.Strings.submit) else stringResource(SharedRes.Strings.next)
        ERPButton(
            modifier = Modifier.fillMaxWidth(),
            text =text,
            onClick = onAction
        )
    }
}

@Composable
fun OthersDetailContent(
    item: EditProfileUI,
    onAction: (EditProfileViewAction) -> Unit,
    state: EditProfileScreenState
) {

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
    ) {
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.bloodGroup,
            label = stringResource(SharedRes.Strings.bloodGroup),
            hint = stringResource(SharedRes.Strings.bloodGroup),
            onValueChange = {
                onAction(EditProfileViewAction.OnBloodGroupChanged(it))
            },
            maxLength = 100,
            onErrorStateChange = {
//                onAction(EditProfileViewAction.BloodGroupError(null))
            },

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.guardianName,
            label = stringResource(SharedRes.Strings.guardian_name),
            hint = stringResource(SharedRes.Strings.guardian_name),
            onValueChange = {
                onAction(EditProfileViewAction.OnGuardianNameChanged(it))
            },
            maxLength = 100,
            onErrorStateChange = {
//                onAction(EditProfileViewAction.GuardianNameError(null))
            },

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.guardianPhone,
            label = stringResource(SharedRes.Strings.guardian_phone),
            hint = stringResource(SharedRes.Strings.guardian_phone),
            onValueChange = {
                onAction(EditProfileViewAction.OnGuardianPhoneChanged(it))
            },
            maxLength = 100,
            onErrorStateChange = {
//                onAction(EditProfileViewAction.GuardianPhoneError(null))
            },

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.designation,
            label = stringResource(SharedRes.Strings.labelDesignation),
            hint = stringResource(SharedRes.Strings.labelDesignation),
            onValueChange = {

            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,
        )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.levelName,
            label = stringResource(SharedRes.Strings.labelLevel),
            hint = stringResource(SharedRes.Strings.labelLevel),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,

            )
        DropDownText(
            label = labelContract,
            hint = stringResource(labelContract),
            selectedValue = item.employeeTypes,
            error = null,
            listOfItems = state.itemList,
            itemClicked = {
                onAction(EditProfileViewAction.EmployeeType(it))
            },
            onError = { err ->
                // handle error
            }
        )

        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.panNumber,
            label = stringResource(SharedRes.Strings.labelPanNumber),
            hint = stringResource(SharedRes.Strings.labelPanNumber),
            onValueChange = {
                onAction(EditProfileViewAction.PANNumberChanged(it))
            },
            maxLength = 100,
            onErrorStateChange = {
//                onAction(EditProfileViewAction.PanNumberError(null))
            },

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.pfNumber,
            label = stringResource(SharedRes.Strings.labelPfNumber),
            hint = stringResource(SharedRes.Strings.labelPfNumber),
            onValueChange = {
                onAction(EditProfileViewAction.PFNumberChanged(it))
            },
            maxLength = 100,
            onErrorStateChange = {
//                onAction(EditProfileViewAction.PFNumberError(null))
            },

            )

        SubmitButton(
            onAction = {
                onAction(EditProfileViewAction.Submit(employeeId = state.employeeId))
            },
            isSubmit = true
        )
    }
}

@Composable
fun PersonalDetailsContent(
    item: EditProfileUI,
    state: EditProfileScreenState,
    onAction: (EditProfileViewAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
    ) {
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.fullName,
            label = stringResource(SharedRes.Strings.username),
            hint = stringResource(SharedRes.Strings.username),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,


            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.address,
            label = stringResource(SharedRes.Strings.address),
            hint = stringResource(SharedRes.Strings.address),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.email,
            label = stringResource(SharedRes.Strings.email),
            hint = stringResource(SharedRes.Strings.email),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,


            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.phoneNumber,
            label = stringResource(SharedRes.Strings.phone),
            hint = stringResource(SharedRes.Strings.phone),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.branchName,
            label = stringResource(SharedRes.Strings.address),
            hint = stringResource(SharedRes.Strings.address),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.employeeId.toString(),
            label = stringResource(SharedRes.Strings.address),
            hint = stringResource(SharedRes.Strings.address),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.maritalStatus,
            label = stringResource(SharedRes.Strings.marital_status),
            hint = stringResource(SharedRes.Strings.marital_status),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,

            )
        ERPTextField(
            modifier = Modifier.fillMaxWidth(),
            text = item.gender,
            label = stringResource(SharedRes.Strings.gender),
            hint = stringResource(SharedRes.Strings.gender),
            onValueChange = {
            },
            maxLength = 100,
            onErrorStateChange = {
            },
            readOnly = true,

            )
        ERPDateTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.profileSummaryList?.dateOfBirth,
            label = stringResource(SharedRes.Strings.date_of_birth),
            hint = stringResource(SharedRes.Strings.date_of_birth),
            rules = FormValidate.requiredValidationRules,
            error = null,
            selectableDates = FutureAndTodayDate,
            onErrorStateChange = {},
            onDateSelected = {
                onAction(EditProfileViewAction.DateOfBirth(it))
            }

        )
        ERPDateTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.profileSummaryList?.joinedDate,
            label = stringResource(SharedRes.Strings.labelJoinedDate),
            hint = stringResource(SharedRes.Strings.labelJoinedDate),
            rules = FormValidate.requiredValidationRules,
            error = null,
            selectableDates = FutureAndTodayDate,
            onErrorStateChange = {},
            onDateSelected = {
                onAction(EditProfileViewAction.JoinedDate(it))
            }

        )
        SubmitButton(
            onAction = {
                onAction(EditProfileViewAction.OnItemSelected(1))
            },
            isSubmit = false
        )

    }


}

@Composable
fun EditProfileTabRow(
    selectedIndex: Int,
    items: List<String>,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.dimens.small1),
        divider = {},
        indicator = {},
    ) {
        items.forEachIndexed { index, title ->
            val isSelected = selectedIndex == index
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.veryLightGray
            }
            val textColor = if (isSelected) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.primaryTextColor
            }

            Tab(
                modifier = Modifier
                    .background(backgroundColor),
                selected = isSelected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = textColor
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}