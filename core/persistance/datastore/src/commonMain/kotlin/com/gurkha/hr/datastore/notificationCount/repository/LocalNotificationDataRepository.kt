package com.gurkha.hr.datastore.notificationCount.repository

import com.gurkha.hr.datastore.notificationCount.local.NotificationCountDataStore
import com.gurkha.hr.datastore.notificationCount.model.NotificationCountData
import kotlinx.coroutines.flow.Flow

class LocalNotificationDataRepository (
    private val notificationCountDataStore: NotificationCountDataStore,
): NotificationDataRepository{
    override val notificationDataFlow: Flow<NotificationCountData> = notificationCountDataStore.notificationFlow
    override suspend fun saveNotificationData(notificationData: NotificationCountData) {
        notificationCountDataStore.update(notificationCount = notificationData)
    }
}