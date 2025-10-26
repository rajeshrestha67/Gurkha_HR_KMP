package com.gurkha.model.chat.list

import kotlinx.serialization.Serializable

@Serializable
data class EmployListResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<EmptyListItemResponseDto>? = null
)

@Serializable
data class EmptyListItemResponseDto(
    val employeeId: Long? = null,
    val chatId: String? = null,
    val branchName: String? = null,
    val employeeName: String? = null,
    val profileImageUrl: String? = null,
    val lastMessage: String? = null,
    val lastMessageSendUser: String? = null,
    val hasUnReadMessage: String? = null,
    val sortOrder: Int? = null
)

