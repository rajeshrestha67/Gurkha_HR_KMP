package com.gurkha.hr.domain.notification.notificationCount.useCase

import com.gurkha.hr.datastore.notificationCount.model.NotificationTotalCountData
import com.gurkha.hr.datastore.notificationCount.repository.NotificationCountDataRepository
import com.gurkha.hr.domain.notification.notificationCount.mapper.toData
import com.gurkha.hr.domain.notification.notificationCount.mapper.toDetail
import com.gurkha.hr.domain.notification.notificationCount.model.NotificationCountData
import com.gurkha.hr.domain.notification.notificationData.repository.NotificationRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class NotificationCountUseCase(
    private val notificationRemoteRepository: NotificationRemoteRepository,
) {
    suspend operator fun invoke(force: Boolean = false): ERPResult<NotificationCountData, DataError> {
        return notificationRemoteRepository.getNotificationCount().map {
            it.toData()
        }
    }
}