package com.gurkha.hr.login.model

import org.jetbrains.compose.resources.StringResource

data class LoginScreenState(
    val username: String = "",
    val password: String = "",
//    val password: String = "Test123@",
//    val username: String = "jyoti.sah@mbank.com.np",
//    val password: String = "Test123@",
//    val username: String = "suneelshrestha9@gmail.com",
//    val password: String = "Soci@lmedia07",
//    val username: String = "chirag.dangol@mbank.com.np",
//    val password: String = "test321",
    val usernameError: StringResource? = null,
    val passwordError: StringResource? = null,
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isBiometricEnabled : Boolean = false,

)
