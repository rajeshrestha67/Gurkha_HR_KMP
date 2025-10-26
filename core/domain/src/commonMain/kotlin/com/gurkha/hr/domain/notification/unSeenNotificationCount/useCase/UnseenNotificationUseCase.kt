package com.gurkha.hr.domain.notification.unSeenNotificationCount.useCase

import com.gurkha.hr.domain.notification.notificationData.repository.NotificationRemoteRepository
import com.gurkha.hr.domain.notification.unSeenNotificationCount.mapper.toData
import com.gurkha.hr.domain.notification.unSeenNotificationCount.model.UnSeenNotificationCountData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class UnseenNotificationUseCase(
    private val notificationRemoteRepository: NotificationRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<UnSeenNotificationCountData, DataError>{
        return notificationRemoteRepository.getAllUnseenNotification().map {
            it.toData()
        }
    }
}