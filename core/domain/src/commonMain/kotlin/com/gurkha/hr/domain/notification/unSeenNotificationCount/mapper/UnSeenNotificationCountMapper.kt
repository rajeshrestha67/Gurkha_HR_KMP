package com.gurkha.hr.domain.notification.unSeenNotificationCount.mapper

import com.gurkha.hr.domain.notification.unSeenNotificationCount.model.UnSeenNotificationCountData
import com.gurkha.model.notification.unSeenNotificationCount.UnSeenNotificationCountDto

fun UnSeenNotificationCountDto.toData(): UnSeenNotificationCountData{
    return UnSeenNotificationCountData(
        count = detail ?: 0
    )
}