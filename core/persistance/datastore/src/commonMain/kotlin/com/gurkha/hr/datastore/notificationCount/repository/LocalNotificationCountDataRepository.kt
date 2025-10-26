package com.gurkha.hr.datastore.notificationCount.repository

import com.gurkha.hr.datastore.notificationCount.local.NotificationCountDataStore
import com.gurkha.hr.datastore.notificationCount.model.NotificationTotalCountData
import kotlinx.coroutines.flow.Flow

class LocalNotificationCountDataRepository (
    private val notificationCountDataStore: NotificationCountDataStore,
): NotificationCountDataRepository{
    override val notificationDataFlow: Flow<NotificationTotalCountData> = notificationCountDataStore.notificationFlow


    override suspend fun updateNotificationData(notificationTotalCountData: NotificationTotalCountData) {
        notificationCountDataStore.update(notificationTotalCountData =notificationTotalCountData )
    }
}