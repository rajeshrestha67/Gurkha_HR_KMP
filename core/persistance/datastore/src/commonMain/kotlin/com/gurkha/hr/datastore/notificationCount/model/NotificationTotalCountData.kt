package com.gurkha.hr.datastore.notificationCount.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationTotalCountData(
    val count: Int = 0
)
