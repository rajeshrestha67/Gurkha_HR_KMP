package com.gurkha.hr.data.notification

import com.gurkha.hr.domain.notification.notificationData.repository.NotificationRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.notification.notificationCount.NotificationCountResponseDto
import com.gurkha.model.notification.notificationData.NotificationsResponseDto
import io.ktor.client.HttpClient

class KtorNotificationRemoteRepository(
    private val httpClient: HttpClient
) : NotificationRemoteRepository{
    override suspend fun getNotificationCount(): ERPResult<NotificationCountResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.TOTAL_NOTIFICATION_COUNT_END_POINT
            )
        }
    }

    override suspend fun getAllNotification(): ERPResult<NotificationsResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.ALL_NOTIFICATION_END_POINT
            )
        }
    }
}