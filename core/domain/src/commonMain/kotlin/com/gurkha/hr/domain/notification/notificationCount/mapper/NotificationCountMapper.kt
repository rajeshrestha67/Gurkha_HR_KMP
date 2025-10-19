package com.gurkha.hr.domain.notification.notificationCount.mapper

import com.gurkha.hr.domain.notification.notificationCount.model.NotificationCountData
import com.gurkha.model.notification.notificationCount.NotificationCountResponseDto

fun NotificationCountResponseDto.toData(): NotificationCountData{
    return NotificationCountData(
        count = detail ?: 0
    )
}