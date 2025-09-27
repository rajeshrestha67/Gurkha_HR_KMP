package com.gurkha.hr.login.model

import org.jetbrains.compose.resources.StringResource

data class LoginScreenState(
    val username: String = "shreejesh.mbank@gmail.com",
    val password: String = "Test@1234",
    val usernameError: StringResource? = null,
    val passwordError: StringResource? = null,
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false

)
