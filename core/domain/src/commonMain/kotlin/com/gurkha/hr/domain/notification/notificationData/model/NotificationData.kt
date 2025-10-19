package com.gurkha.hr.domain.notification.notificationData.model

data class NotificationData(
    val id: Int,
    val actionPerformerId: Int,
    val actionTargetId: Int,
    val actionPerformerName: String,
    val actionTargetName: String,
    val remarks: String,
    val actionField: String,
    val actionType: String,
    val actionDate: String,
    val actionTime: String,
    val seen: Boolean,
    val imageUrl: String,
    val initials: String
)
