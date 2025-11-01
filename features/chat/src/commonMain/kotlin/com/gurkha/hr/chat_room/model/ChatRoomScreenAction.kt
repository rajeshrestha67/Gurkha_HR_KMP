package com.gurkha.hr.chat_room.model

sealed interface ChatRoomScreenAction {
    data class UpdateChatData(val json: String) : ChatRoomScreenAction
    object Send : ChatRoomScreenAction
    data class MessageChanged(val message: String) : ChatRoomScreenAction

    data class OnTyping(val isTyping: Boolean) : ChatRoomScreenAction

    data object OnRefresh: ChatRoomScreenAction

}