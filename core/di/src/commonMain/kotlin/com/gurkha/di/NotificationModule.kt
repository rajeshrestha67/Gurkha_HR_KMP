package com.gurkha.di

import com.gurkha.hr.components.permissions.ProgressNotification
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class NotificationModule {

    @Factory
    fun getNotificationProgress(): ProgressNotification = ProgressNotification()
}


