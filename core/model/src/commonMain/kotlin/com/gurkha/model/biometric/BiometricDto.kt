package com.gurkha.model.biometric

import kotlinx.serialization.Serializable

@Serializable
data class BiometricResponseDto(
    val message: String? = null
)

@Serializable
data class BiometricRequestDto(
    val uid: String,
    val biometricToken: String
)