package com.gurkha.hr.model

import com.gurkha.hr.domain.chat.model.EmployChatItem
import com.gurkha.model.chat.ChatTypeEnum

sealed interface ChatScreenAction {
    object SearchClicked : ChatScreenAction
    object ClearSearch : ChatScreenAction
    data class SearchQueryChanged(val query: String) : ChatScreenAction

    data class ItemClick(val chatItem: EmployChatItem) : ChatScreenAction
    data object OnEmployeeRefresh : ChatScreenAction

    object Send : ChatScreenAction
    data class MessageChanged(val message: String) : ChatScreenAction

    data object OnChatRefresh : ChatScreenAction

    data class UpdateCurrentEmploy(val json: String) : ChatScreenAction

    data class OnChatTypeChange(val chatType: ChatTypeEnum): ChatScreenAction

}