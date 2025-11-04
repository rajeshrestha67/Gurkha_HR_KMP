package com.gurkha.model.uploadImage

import kotlinx.serialization.Serializable

@Serializable
data class UploadImageResponseDto(
    val apiErrors: String? = null,
    val success: Boolean? = null,
    val message: String? = null,
    val code: String? = null,
    val data: UploadImageResponseDtoData? = null
)

@Serializable
data class UploadImageResponseDtoData(
    val fileNames: List<String>? = null
)

@Serializable
data class UploadImageRequestDto(
    val employeeId: Int,
    val type: String
)