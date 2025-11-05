package com.gurkha.hr.domain.notification.notificationCount.useCase

import com.gurkha.hr.domain.notification.notificationCount.mapper.toData
import com.gurkha.hr.domain.notification.notificationCount.model.NotificationCountData
import com.gurkha.hr.domain.notification.notificationData.repository.NotificationRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class NotificationCountUseCase(
    private val notificationRemoteRepository: NotificationRemoteRepository,
) {
    suspend operator fun invoke(force: Boolean = false): ERPResult<NotificationCountData, DataError> {
        return notificationRemoteRepository.getNotificationCount().map {
            it.toData()
        }
    }
}