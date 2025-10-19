package com.gurkha.hr.notification.model

import com.gurkha.hr.domain.notification.notificationData.model.NotificationData

data class NotificationState(
    val notifications : List<NotificationData> = emptyList(),
    val notificationGrouped : Map<String,List<NotificationData>> = emptyMap(),
    val isNotificationLoading : Boolean = false,
)
