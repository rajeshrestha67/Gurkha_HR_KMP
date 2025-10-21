package com.gurkha.hr.datastore.notificationCount.repository

import com.gurkha.hr.datastore.notificationCount.model.NotificationTotalCountData
import kotlinx.coroutines.flow.Flow

interface NotificationCountDataRepository {
    val notificationDataFlow: Flow<NotificationTotalCountData>
    suspend fun updateNotificationData(notificationTotalCountData: NotificationTotalCountData)

}