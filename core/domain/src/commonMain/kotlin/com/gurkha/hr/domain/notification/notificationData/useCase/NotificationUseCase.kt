package com.gurkha.hr.domain.notification.notificationData.useCase

import com.gurkha.hr.domain.notification.notificationData.mapper.toData
import com.gurkha.hr.domain.notification.notificationData.model.NotificationData
import com.gurkha.hr.domain.notification.notificationData.repository.NotificationRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class NotificationUseCase(
    private val notificationRemoteRepository: NotificationRemoteRepository,
) {
    suspend operator fun invoke(): ERPResult<List<NotificationData>, DataError>{
        return notificationRemoteRepository.getAllNotification().map {
            it.toData()
        }
    }
}