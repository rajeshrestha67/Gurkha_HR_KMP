package com.gurkha.model.changePassword

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequestDTO(
    val email: String = "Shreejesh.mbank@gmail.com",
    val password: String,
    val confirmPassword: String,

)

data class ChangePasswordResponseDTO(
    val status: String?,
    val message: String?,
    val success: Boolean,
)
