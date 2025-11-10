package com.gurkha.model.support

import kotlinx.serialization.Serializable

@Serializable
data class SupportResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<SupportContactDetail>? = null,
    val success: Boolean? = null
)

@Serializable
data class SupportContactDetail(
    val phoneNumber: String ?= null,
    val coopName: String ?= null,
    val chatId: String? = null
)
