package com.gurkha.hr.domain.chat.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class ChatMessageData(
    val metaData: ChatMessageMetaData?,
    val messages: List<ChatMessageContent>
)

data class ChatMessageMetaData(
    val currentPage: Int = 0,
    val totalPages: Int = 1
)

data class ChatMessageContent @OptIn(ExperimentalTime::class) constructor(
    val message: String,
    val date: Instant?,
    val fromMe: Boolean
)
