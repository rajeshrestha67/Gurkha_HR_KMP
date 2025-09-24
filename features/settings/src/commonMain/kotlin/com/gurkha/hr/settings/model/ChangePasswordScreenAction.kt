package com.gurkha.hr.settings.model

import org.jetbrains.compose.resources.StringResource

sealed interface ChangePasswordScreenAction {
    data class OnNewPasswordChanged(val newPassword: String) : ChangePasswordScreenAction

    data class OnConfirmPasswordChanged(val confirmPassword: String) : ChangePasswordScreenAction

    data class OnNewPasswordError(val newPasswordError: StringResource?) : ChangePasswordScreenAction

    data class OnConfirmPasswordError(val confirmPasswordError: StringResource?) : ChangePasswordScreenAction

    data object ConfirmClicked : ChangePasswordScreenAction
}