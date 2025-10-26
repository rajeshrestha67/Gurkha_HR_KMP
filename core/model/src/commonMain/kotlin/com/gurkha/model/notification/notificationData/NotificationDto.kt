package com.gurkha.model.notification.notificationData

import kotlinx.serialization.Serializable

@Serializable
data class NotificationsResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<NotificationDetailDto>? = null,
    val success: Boolean? = null
)

@Serializable
data class NotificationDetailDto(
    val id: Int? = null,
    val actionPerformerId: Int? = null,
    val actionTargetId: Int? = null,
    val actionPerformerName: String? = null,
    val actionTargetName: String? = null,
    val remarks: String? = null,
    val actionField: String? = null,
    val actionType: String? = null,
    val actionDate: String? = null,
    val actionTime: String? = null,
    val seen: String? = null,
    val imageUrl: String? = null
)