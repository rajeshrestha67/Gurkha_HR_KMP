package com.gurkha.hr.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.PlatformMessage
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.PasswordTextField
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.settings.model.ChangePasswordScreenAction
import com.gurkha.hr.settings.model.ChangePasswordScreenState
import com.gurkha.hr.settings.model.ChangePasswordViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onBackPressed: () -> Unit,
) {

    val changePasswordViewModel: ChangePasswordViewModel = koinViewModel()
    val state by changePasswordViewModel.state.collectAsStateWithLifecycle()


    val platformMessage: PlatformMessage = koinInject()

    LaunchedEffect(Unit) {
        changePasswordViewModel.successChannel.collect { success ->
            if (success) {
                onBackPressed()
            }
        }
    }

    LaunchedEffect(Unit) {
        changePasswordViewModel.errorChannel.collect { error ->
            platformMessage.showToast(error)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,


        topBar = {
            TopAppBar(
                title = { Text(stringResource(SharedRes.Strings.change_password)) },
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
        ChangePasswordScreenContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            onAction = changePasswordViewModel::onAction,
        )

    }
}

@Composable
fun ChangePasswordScreenContainer(
    modifier: Modifier = Modifier,
    state: ChangePasswordScreenState,
    onAction: (ChangePasswordScreenAction) -> Unit
) {
    Column(
        modifier = modifier.padding(MaterialTheme.dimens.small3),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
    ) {
        PasswordTextField(
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(SharedRes.Strings.new_password),
            hint = stringResource(SharedRes.Strings.new_password),
            onValueChange = { newValue ->
                onAction(ChangePasswordScreenAction.OnNewPasswordChanged(newPassword = newValue))
            },
            value = state.newPassword,

            onErrorStateChange = { newError ->
                onAction(ChangePasswordScreenAction.OnNewPasswordError(newPasswordError = newError))
            },
            imeAction = ImeAction.Send,
            error = state.newPasswordError,
            keyboardActions = KeyboardActions(
                onSend = {

                }
            ),
            rules = FormValidate.passwordValidationRules
        )

        PasswordTextField(
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.dimens.small2),

            label = stringResource(SharedRes.Strings.confirm_password),
            hint = stringResource(SharedRes.Strings.confirm_password),
            onValueChange = { confirmValue ->
                onAction(ChangePasswordScreenAction.OnConfirmPasswordChanged(confirmPassword = confirmValue))

            },
            value = state.confirmPassword,

            onErrorStateChange = { confirmError ->
                onAction(ChangePasswordScreenAction.OnConfirmPasswordError(confirmPasswordError = confirmError))
            },
            imeAction = ImeAction.Send,
            error = state.confirmPasswordError,
            keyboardActions = KeyboardActions(
                onSend = { }),
            rules = FormValidate.passwordValidationRules

        )

        ERPButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.dimens.small3),
            onClick = {
                onAction(ChangePasswordScreenAction.ConfirmClicked)
            },
            isLoading = state.isLoading,
            text = stringResource(SharedRes.Strings.confirm)
        )


    }
}

