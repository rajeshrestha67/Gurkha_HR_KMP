package com.gurkha.hr.domain.chat.model

import androidx.compose.ui.graphics.Color


data class ChatItem(
    val employeeId: Long,
    val chatId: String,
    val branchName: String,
    val employeeName: String,
    val profileImageUrl: String?,
    val lastMessage: String,
    val lastMessageSendUser: String,
    val hasUnReadMessage: String,
    val sortOrder: Int,
    val nameInitials: String,
    val backgroundColor: Color,
    val phoneNumber: String?
)
