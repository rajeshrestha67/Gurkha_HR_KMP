package com.gurkha.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatUserData(
    val employeeId: Long,
    val chatId: String,
    val branchName: String,
    val employeeName: String,
    val profileImageUrl: String?,
    val nameInitials: String,
    val backgroundColor: ULong,
    val phoneNumber: String?
)
