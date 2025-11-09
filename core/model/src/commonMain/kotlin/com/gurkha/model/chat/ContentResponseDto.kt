package com.gurkha.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class ContentData(
    val type: String,
    val chatId: String,
    val fromUser: String,
    val content: String
)

@Serializable
data class UserStatusChangeData(
    val status: Boolean,
    val chatId: String
)
