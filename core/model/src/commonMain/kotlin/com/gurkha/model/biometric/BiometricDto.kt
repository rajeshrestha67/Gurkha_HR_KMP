package com.gurkha.model.biometric

import kotlinx.serialization.Serializable

@Serializable
data class BiometricResponseDto(
    val message : String ? = null
)

@Serializable
data class BiometricRequestDto(
    val uid: Int,
    val biometricToken: String
)