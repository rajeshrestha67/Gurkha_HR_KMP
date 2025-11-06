package com.gurkha.hr.login.model

import org.jetbrains.compose.resources.StringResource

sealed interface LoginScreenAction {
    data class OnUsernameChanged(val username: String) : LoginScreenAction
    data class OnPasswordChanged(val password: String) : LoginScreenAction
    data class OnUsernameError(val usernameError: StringResource?) : LoginScreenAction
    data class OnPasswordError(val passwordError: StringResource?) : LoginScreenAction

    data object LoginClicked : LoginScreenAction

    data object OnBiometricLogin : LoginScreenAction

    data object OnResetBiometric: LoginScreenAction
}