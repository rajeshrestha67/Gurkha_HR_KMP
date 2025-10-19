package com.gurkha.hr.domain.notification.notificationData.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.notification.notificationCount.NotificationCountResponseDto
import com.gurkha.model.notification.notificationData.NotificationsResponseDto

interface NotificationRemoteRepository {
    suspend fun getNotificationCount(): ERPResult<NotificationCountResponseDto, DataError>

    suspend fun getAllNotification(): ERPResult<NotificationsResponseDto, DataError>
}