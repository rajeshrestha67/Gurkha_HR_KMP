package com.gurkha.hr.domain.notification.notificationData.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.notification.notificationCount.NotificationCountResponseDto
import com.gurkha.model.notification.notificationData.NotificationsResponseDto
import com.gurkha.model.notification.unSeenNotificationCount.UnSeenNotificationCountDto

interface NotificationRemoteRepository {
    suspend fun getNotificationCount(): ERPResult<NotificationCountResponseDto, DataError>

    suspend fun getAllNotification(offset: Int): ERPResult<NotificationsResponseDto, DataError>

    suspend fun getAllUnseenNotification(): ERPResult<UnSeenNotificationCountDto, DataError>
}