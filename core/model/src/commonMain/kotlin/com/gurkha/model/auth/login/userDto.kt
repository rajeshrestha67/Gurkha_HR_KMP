package com.gurkha.model.auth.login

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequestDto(
    val email:String,
    val password: String
)

@Serializable
data class LoginResponseDto(
    val token:String? = null,
    val role: String? = null
)