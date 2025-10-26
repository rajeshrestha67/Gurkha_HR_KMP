package com.gurkha.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val type: String,
    val chatId: String,
    val fromUser: String,
    val content: String
)