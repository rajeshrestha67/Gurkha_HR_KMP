package com.gurkha.hr.chat_room.model

import com.gurkha.hr.domain.chat.mapper.toChatFormattedData
import com.gurkha.hr.domain.chat.mapper.toChatFormattedTime
import com.gurkha.hr.domain.chat.model.ChatMessageContent
import com.gurkha.hr.domain.chat.model.ChatMessageMetaData
import com.gurkha.model.chat.ChatUserData
import kotlin.random.Random
import kotlin.time.ExperimentalTime

data class ChatRoomScreenState(
    val isLoading: Boolean = false,
    val isTyping: Boolean = false,
    val chatUserData: ChatUserData? = null,
    val message: String = "",
    val messages: LinkedHashMap<String, List<ChatMessage>> = LinkedHashMap(),
    val metaData: ChatMetaData? = null,
    val isSocketConnected: Boolean = false
)


data class ChatMessage @OptIn(ExperimentalTime::class) constructor(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val message: String,
    val date: String,
    val time: String,
    val fromMe: Boolean
)

data class ChatMetaData(
    val currentPage: Int = 0,
    val totalPages: Int = 1
)

@OptIn(ExperimentalTime::class)
fun ChatMessageContent.toMessage(): ChatMessage {
    return ChatMessage(
        message = message,
        date = date.toChatFormattedData(),
        fromMe = fromMe,
        time = date.toChatFormattedTime()
    )
}

fun ChatMessageMetaData.toChatMetaData(): ChatMetaData {
    return ChatMetaData(
        currentPage = currentPage,
        totalPages = totalPages
    )
}
