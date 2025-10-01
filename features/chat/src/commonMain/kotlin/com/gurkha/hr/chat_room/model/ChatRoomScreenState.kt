package com.gurkha.hr.chat_room.model

import com.gurkha.model.chat.ChatUserData

data class ChatRoomScreenState(
    val isLoading: Boolean = false,
    val isTyping: Boolean = false,
    val chatUserData: ChatUserData? = null,
    val message: String = ""
)
