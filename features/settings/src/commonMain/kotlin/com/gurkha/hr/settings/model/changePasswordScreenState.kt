package com.gurkha.hr.settings.model

import org.jetbrains.compose.resources.StringResource

data class ChangePasswordScreenState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val newPasswordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
    val isLoading: Boolean = false
)