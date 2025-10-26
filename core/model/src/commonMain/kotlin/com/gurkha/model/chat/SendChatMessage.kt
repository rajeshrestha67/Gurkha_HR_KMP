package com.gurkha.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class SendChatMessage(
    val chatId: String,
    val message: String,
    val fromUser: String
)