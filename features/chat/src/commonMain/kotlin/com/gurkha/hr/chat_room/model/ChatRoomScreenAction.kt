package com.gurkha.hr.chat_room.model

sealed interface ChatRoomScreenAction {
    data class UpdateChatData(val json: String) : ChatRoomScreenAction
    object ClearSearch : ChatRoomScreenAction
    object Send : ChatRoomScreenAction
    data class SearchQueryChanged(val message: String) : ChatRoomScreenAction

}