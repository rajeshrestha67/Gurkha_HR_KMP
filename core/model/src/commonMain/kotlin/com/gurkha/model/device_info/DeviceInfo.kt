package com.gurkha.model.device_info

import kotlinx.serialization.Serializable

@Serializable
data class DeviceInfo(
    val uid: String,
    val platform: String,
    val manufacturer: String?,
    val model: String?,
    val osVersion: String?,
    val sdkInt: String?,
    val locale: String?,
    val timezone: String?,
    val appVersion: String?,
    val appBuild: String?,
    val deviceName: String?,
    val isEmulator: Boolean
)