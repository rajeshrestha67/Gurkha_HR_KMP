package com.gurkha.di

import com.gurkha.hr.domain.notification.notificationData.useCase.NotificationUseCase
import com.gurkha.hr.notification.NotificationViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class NotificationModule {
    @KoinViewModel
    fun getNotificationViewModel(
        notificationUseCase: NotificationUseCase
    ): NotificationViewModel= NotificationViewModel(
        notificationUseCase = notificationUseCase
    )
}