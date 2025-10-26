package com.gurkha.model.notification.unSeenNotificationCount

import kotlinx.serialization.Serializable

@Serializable
data class UnSeenNotificationCountDto(
    val status: String? = null,
    val message: String? = null,
    val detail: Int? = null,
    val success: Boolean? = null
)
