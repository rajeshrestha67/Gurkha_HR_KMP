package com.gurkha.hr.datastore.notificationCount.repository

import com.gurkha.hr.datastore.notificationCount.model.NotificationCountData
import kotlinx.coroutines.flow.Flow

interface NotificationDataRepository {
    val notificationDataFlow: Flow<NotificationCountData>

    suspend fun saveNotificationData(notificationData: NotificationCountData)
}