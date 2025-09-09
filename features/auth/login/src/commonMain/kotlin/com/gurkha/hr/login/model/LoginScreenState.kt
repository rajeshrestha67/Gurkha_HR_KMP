package com.gurkha.hr.login.model

import org.jetbrains.compose.resources.StringResource

data class LoginScreenState(
    val username: String = "Chirag.dangol@mbank.com.np",
    val password: String = "OVf#9PfTs",
    val usernameError: StringResource? = null,
    val passwordError: StringResource? = null,
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false

)
