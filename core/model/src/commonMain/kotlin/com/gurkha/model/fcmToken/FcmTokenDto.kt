package com.gurkha.model.fcmToken

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenResponseDto(
    val message: String ?= null,
    val success: Boolean ?= null,
)

@Serializable
data class FcmTokenRequestDto(
    val fcmToken : String,
    val uid : String
)
