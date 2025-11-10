package com.gurkha.hr.profile.edit_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ERPDateTextField
import com.gurkha.hr.components.date.FutureAndTodayDate
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.hideKeyboardOnTap
import com.gurkha.hr.components.loadingScreen.LoadingScreen
import com.gurkha.hr.components.prompts.PromptModalBottomSheet
import com.gurkha.hr.components.prompts.PromptType
import com.gurkha.hr.components.tabbar.ERPTabView
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.domain.userDetail.ui.EditProfileUI
import com.gurkha.hr.profile.model.edit_profile_screen.EditProfileScreenState
import com.gurkha.hr.profile.model.edit_profile_screen.EditProfileViewAction
import com.gurkha.hr.profile.model.edit_profile_screen.Title
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.SharedRes.Strings.labelContract
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreen(
    onBackPressed: () -> Unit,
) {
    val viewModel: EditProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showSuccessModal by remember { mutableStateOf(false) }
    var showErrorModal by remember { mutableStateOf(false) }
    var messageToShow by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.successChannel.collect {
            showSuccessModal = true
            messageToShow = it
        }
    }

    LaunchedEffect(Unit) {
        viewModel.errorChannel.collect {
            showErrorModal = true
            messageToShow = it
        }
    }

    EditProfileScreenContainer(
        onBackPressed = onBackPressed,
        state = state,
        onAction = viewModel::onAction,
        showSuccessModal = showSuccessModal,
        showErrorModal = showErrorModal,
        messageToShow = messageToShow,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenContainer(
    showSuccessModal: Boolean,
    showErrorModal: Boolean,
    messageToShow: String,
    onBackPressed: () -> Unit,
    onAction: (EditProfileViewAction) -> Unit,
    state: EditProfileScreenState
) {


    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        modifier = Modifier.fillMaxSize().hideKeyboardOnTap(
            focusManager = focusManager,
            keyboardController = keyboardController
        ),
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

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (state.isUpdating) {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                        .clickable(onClick = {}),
                )
            }
            EditProfileScreenContent(
                modifier = Modifier
                    .fillMaxSize().padding(paddingValues),
                state = state,
                onAction = onAction,
                showErrorModal = showErrorModal,
                showSuccessModal = showSuccessModal,
                messageToShow = messageToShow,
                onBackPressed = onBackPressed
            )
        }
    }
}

@Composable
fun EditProfileScreenContent(
    showSuccessModal: Boolean,
    showErrorModal: Boolean,
    messageToShow: String,
    modifier: Modifier = Modifier,
    state: EditProfileScreenState,
    onAction: (EditProfileViewAction) -> Unit,
    onBackPressed: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(
                vertical = MaterialTheme.dimens.small2,
                horizontal = MaterialTheme.dimens.small3
            )
    ) {

        stickyHeader {
            EditProfileRow(
                selectedTab = state.selectedTab,
                items = state.editList,
                onTabSelected = { index ->
                    onAction(EditProfileViewAction.OnItemSelected(index))
                }

            )
        }

        when (state.selectedTab) {
            Title.PersonalDetails -> {

                state.profileSummaryList?.let {
                    item {
                        PersonalDetailsContent(
                            item = state.profileSummaryList,
                            onAction = onAction,
                            state = state
                        )
                    }
                }
            }

            Title.OthersDetails -> {
                state.profileSummaryList?.let {
                    item {
                        OthersDetailContent(
                            item = state.profileSummaryList,
                            onAction = onAction,
                            state = state
                        )
                    }
                }
            }
        }

        if (showSuccessModal) {
            item {
                PromptModalBottomSheet(
                    text = messageToShow,
                    onBackPressed = onBackPressed
                )
            }
        }

        if (showErrorModal) {
            item {
                PromptModalBottomSheet(
                    text = messageToShow,
                    promptType = PromptType.FAILED,
                    onBackPressed = onBackPressed
                )
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
        val text =
            if (isSubmit) stringResource(SharedRes.Strings.submit) else stringResource(SharedRes.Strings.next)
        ERPButton(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            onClick = onAction,
        )
    }
}

@Composable
fun OthersDetailContent(
    item: EditProfileUI,
    onAction: (EditProfileViewAction) -> Unit,
    state: EditProfileScreenState
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth(),
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
            text = state.profileSummaryList?.panNumber ?: "",
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
            imeAction = ImeAction.Send,
            maxLength = 100,
            onErrorStateChange = {
            },
            keyboardActions = KeyboardActions(
                onSend = {
                    keyboardController?.hide()
                    onAction(EditProfileViewAction.Submit(employeeId = state.employeeId))

                }
            )

            )

        SubmitButton(
            onAction = {
                keyboardController?.hide()
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
        modifier = Modifier.fillMaxWidth(),
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
            label = stringResource(SharedRes.Strings.branch),
            hint = stringResource(SharedRes.Strings.branch),
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
            label = stringResource(SharedRes.Strings.userId),
            hint = stringResource(SharedRes.Strings.userId),
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
                onAction(EditProfileViewAction.OnItemSelected(Title.OthersDetails))
            },
            isSubmit = false
        )

    }


}

@Composable
fun EditProfileRow(
    selectedTab: Title,
    items: List<Title>,
    onTabSelected: (Title) -> Unit,
    modifier: Modifier = Modifier
) {
    ERPTabView(
        modifier = modifier.fillMaxWidth().height(MaterialTheme.dimens.large),
        items = items,
        selectedTab = selectedTab,
        shape = MaterialTheme.shapes.medium,
        onItemSelected = {
            onTabSelected(it)
        }
    ) { item, isSelected ->
        val color =
            if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
        Text(
            text = stringResource(item.title),
            style = MaterialTheme.typography.titleSmall.copy(
                color = color
            )
        )
    }
}