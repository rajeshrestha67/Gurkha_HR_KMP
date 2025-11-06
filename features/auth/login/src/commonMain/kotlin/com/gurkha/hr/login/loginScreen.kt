package com.gurkha.hr.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.PlatformMessage
import com.gurkha.hr.components.biometric.rememberBiometricPromptLauncher
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.hideKeyboardOnTap
import com.gurkha.hr.components.permissions.POST_NOTIFICATIONS_PERMISSION
import com.gurkha.hr.components.permissions.rememberRequestPermission
import com.gurkha.hr.components.prompts.PromptModalBottomSheet
import com.gurkha.hr.components.prompts.PromptType
import com.gurkha.hr.components.textField.AGEmailTextField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.PasswordTextField
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.login.model.LoginScreenAction
import com.gurkha.hr.login.model.LoginScreenState
import com.gurkha.hr.res.SharedRes
import com.gurkha.model.biometric.BiometricAuthResult
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val TAG = "LoginScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToDashboard: () -> Unit
) {

    val loginViewModel: LoginViewModel = koinViewModel()
    val state by loginViewModel.state.collectAsStateWithLifecycle()

    val platformMessage: PlatformMessage = koinInject()
    var showResetModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        loginViewModel.errorChannel.collect {
            platformMessage.showToast(it)
        }
    }

    LaunchedEffect(Unit) {
        loginViewModel.successChannel.collect {
            if (it) {
                onNavigateToDashboard()
            }
        }
    }

    LaunchedEffect(Unit) {
        loginViewModel.resetChannel.collect {
            if (it) {
                showResetModal = true
            }
        }
    }

    val onPermission = rememberRequestPermission(
        permissions = listOf(
            POST_NOTIFICATIONS_PERMISSION
        ),
        onGranted = { permission ->
            AppLogger.i(
                tag = TAG,
                message = "Permission granted: $permission"
            )
        },
        onDenied = { permission ->
            AppLogger.i(
                tag = TAG,
                message = "Permission denied: $permission"
            )
        },
        onPermanentlyDenied = { permission ->
            AppLogger.i(
                tag = TAG,
                message = "Permission denied permanent: $permission"
            )
        },
        onAllGranted = {
            AppLogger.i(
                tag = TAG,
                message = "All Permission granted"
            )
        })


    LaunchedEffect(Unit) {
        onPermission()
    }
    LoginScreenContent(
        platformMessage = platformMessage,
        state = state,
        onAction = loginViewModel::onAction,
        onDismiss = {
            showResetModal = false
        },
        showResetModal = showResetModal,
        onReset = {}
    )


}

@Composable
fun LoginScreenContent(
    showResetModal: Boolean,
    onDismiss: () -> Unit,
    onReset: () -> Unit,
    platformMessage: PlatformMessage,
    state: LoginScreenState,
    onAction: (LoginScreenAction) -> Unit
) {
    val focusRequester: FocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    //test only
    val launcher = rememberBiometricPromptLauncher(
        onResult = { result ->
            when (result) {
                BiometricAuthResult.Success -> {
                    onAction(LoginScreenAction.OnBiometricLogin)
                }

                is BiometricAuthResult.Error -> {
                    platformMessage.showToast("❌ Error: ${result.message}")
                }

                BiometricAuthResult.Failure -> {
                    platformMessage.showToast("❌ Authentication Failed. Try again.")
                }

                BiometricAuthResult.NotAvailable -> {
                    platformMessage.showToast("⚠️ Biometrics Not Available or Set Up.")

                }
            }
        }
    )

    val title = stringResource(SharedRes.Strings.login_verification)
    val subTitle = stringResource(SharedRes.Strings.auth_using_biometric)
    val negativeText = stringResource(SharedRes.Strings.cancel)



    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding().hideKeyboardOnTap(
            focusManager = focusManager,
            keyboardController = keyboardController
        ),
        containerColor = MaterialTheme.colorScheme.background,
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
        ) {

            AsyncImage(
                modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimens.small3).height(
                    MaterialTheme.dimens.loginImageSize
                ),
                model = SharedRes.getRes("drawable/gurkha_hr.png"),
                contentDescription = "gurkha_hr",
                contentScale = ContentScale.FillWidth
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small3),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)

            ) {

                Text(
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp
                    ),
                    text = stringResource(SharedRes.Strings.welcome)
                )

                AGEmailTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                    label = stringResource(SharedRes.Strings.username),
                    hint = stringResource(SharedRes.Strings.enterYourUsername),
                    onValueChange = {
                        onAction(LoginScreenAction.OnUsernameChanged(it))
                    },
                    value = state.username,
                    error = state.usernameError,
                    enabled = !state.isLoading,
                    onErrorStateChange = {
                        onAction(LoginScreenAction.OnUsernameError(it))
                    },
                    imeAction = ImeAction.Next,
                    rules = FormValidate.emailValidationRules,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                PasswordTextField(
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(SharedRes.Strings.password),
                    hint = stringResource(SharedRes.Strings.enterYourPassword),
                    onValueChange = {
                        onAction(LoginScreenAction.OnPasswordChanged(it))
                    },
                    value = state.password,
                    error = state.passwordError,
                    onErrorStateChange = {
                        onAction(LoginScreenAction.OnPasswordError(it))
                    },
                    imeAction = ImeAction.Send,
                    keyboardActions = KeyboardActions(
                        onSend = {
                            onAction(LoginScreenAction.LoginClicked)
                        }
                    ),
                    rules = FormValidate.passwordValidationRules
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    ERPButton(
                        modifier = Modifier.fillMaxWidth(if (state.isBiometricEnabled) 0.8f else 1f),
                        onClick = {
                            onAction(LoginScreenAction.LoginClicked)
                        },
                        isLoading = state.isLoading,
                        text = stringResource(SharedRes.Strings.login)
                    )
                    //only show if the user has enabled the biometric
                    if (state.isBiometricEnabled) {
                        IconButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                keyboardController?.hide()
                                launcher.launch(
                                    title,
                                    subTitle,
                                    negativeText
                                )
                            },
                            content = {
                                Icon(
                                    Icons.Filled.Fingerprint, contentDescription = "Fingerprint",
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            })
                    }
                }
                if (showResetModal) {
                    ShowResetModal(
                        onDismiss = onDismiss,
                        onReset = {
                            onAction(LoginScreenAction.OnResetBiometric)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowResetModal(
    onDismiss: () -> Unit,
    onReset: () -> Unit
) {
    val sheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    ModalBottomSheet(
        sheetState = sheet,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(MaterialTheme.dimens.promptDialogSize)
                    .aspectRatio(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .padding(MaterialTheme.dimens.medium1)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Fingerprint,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(MaterialTheme.dimens.medium3)
                )
            }
            Text(
                text = stringResource(SharedRes.Strings.please_reset_biometric),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.erpColors.primaryTextColor
                )
            )
            ERPButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onReset, text = stringResource(SharedRes.Strings.reset_biometric)
            )

            ERPButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.error,
                onClick = onDismiss, text = stringResource(SharedRes.Strings.cancel)
            )
        }
    }
}