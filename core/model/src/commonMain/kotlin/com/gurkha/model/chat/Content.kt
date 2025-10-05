package com.gurkha.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val chatId: String,
    val fromUser: String,
    val content: String
)