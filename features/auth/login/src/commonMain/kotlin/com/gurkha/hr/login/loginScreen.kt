package com.gurkha.hr.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.PlatformMessage
import com.gurkha.hr.components.hideKeyboardOnTap
import com.gurkha.hr.components.textField.AGEmailTextField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.PasswordTextField
import com.gurkha.hr.login.model.LoginScreenAction
import com.gurkha.hr.login.model.LoginScreenState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToDashboard: () -> Unit
) {

    val loginViewModel: LoginViewModel = koinViewModel()
    val state by loginViewModel.state.collectAsStateWithLifecycle()

    val platformMessage: PlatformMessage = koinInject()

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
    LoginScreenContent(
        state = state,
        onAction = loginViewModel::onAction
    )

}

@Composable
fun LoginScreenContent(
    state: LoginScreenState,
    onAction: (LoginScreenAction) -> Unit
) {
    val focusRequester: FocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().hideKeyboardOnTap(
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
                modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimens.small3).heightIn(
                    max = 200.dp
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
                        onAction(LoginScreenAction.OnUsernameError(it?.errorMsg))
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
                        onAction(LoginScreenAction.OnPasswordError(it?.errorMsg))
                    },
                    imeAction = ImeAction.Send,
                    keyboardActions = KeyboardActions(
                        onSend = {
                            onAction(LoginScreenAction.LoginClicked)
                        }
                    ),
                    rules = FormValidate.passwordValidationRules
                )

                ERPButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onAction(LoginScreenAction.LoginClicked)
                    },
                    isLoading = state.isLoading,
                    text = stringResource(SharedRes.Strings.login)
                )
            }
        }
    }
}