package com.gurkha.hr.domain.notification.notificationData.mapper

import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.domain.notification.notificationData.model.NotificationData
import com.gurkha.model.notification.notificationData.NotificationsResponseDto

fun NotificationsResponseDto.toData(): List<NotificationData> {
    return detail?.map {
        NotificationData(
            id = it.id ?: 0,
            actionPerformerId = it.actionPerformerId ?: 0,
            actionTargetId = it.actionTargetId ?: 0,
            actionPerformerName = it.actionPerformerName ?: "",
            actionTargetName = it.actionTargetName ?: "",
            remarks = it.remarks ?: "",
            actionField = it.actionField ?: "",
            actionType = it.actionType?.lowercase() ?: "",
            actionDate = it.actionDate ?: "",
            actionTime = it.actionTime ?: "",
            seen = it.seen.equals("Y", ignoreCase = true),
            imageUrl = it.imageUrl?.let { url ->
                "https://mbank.gurkhahr.com/erp-images/$url"
            } ?: "",
            initials = it.actionPerformerName?.extractInitials() ?: ""
        )
    } ?: emptyList()
}
