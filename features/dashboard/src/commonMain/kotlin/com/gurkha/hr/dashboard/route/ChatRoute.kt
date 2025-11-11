package com.gurkha.hr.dashboard.route

import com.gurkha.model.chat.ChatTypeEnum
import kotlinx.serialization.Serializable

@Serializable
sealed interface ChatRoute {
    @Serializable
    data class ChatList(val chatType: ChatTypeEnum) : ChatRoute

    @Serializable
    data class ChatRoom(val json: String) : ChatRoute
}

@Serializable
object ChatGraphRoute