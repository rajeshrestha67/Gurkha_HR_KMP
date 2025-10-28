package com.gurkha.hr.model.notification

import com.gurkha.hr.domain.notification.notificationData.model.NotificationData

data class NotificationState(
    val notifications : List<NotificationData> = emptyList(),
    val notificationGrouped : Map<String,List<NotificationData>> = emptyMap(),
    val isNotificationLoading : Boolean = false,
    val isRefreshing: Boolean = false
)