package com.gurkha.hr.model.notification

interface NotificationAction {
    data object OnRefresh: NotificationAction
    data object OnPagination: NotificationAction
}