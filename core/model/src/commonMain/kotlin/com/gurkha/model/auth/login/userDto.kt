package com.gurkha.model.auth.login

import com.gurkha.model.device_info.DeviceInfo
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class LoginRequestDto(
    val email: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val password: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val biometricToken: String? = null,
    val deviceInfo: DeviceInfo
)

@Serializable
data class LoginResponseDto(
    val token: String? = null,
    val role: String? = null,
    val message: String? = null
)

