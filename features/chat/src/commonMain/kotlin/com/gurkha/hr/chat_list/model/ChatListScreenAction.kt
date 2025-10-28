package com.gurkha.hr.chat_list.model

import com.gurkha.hr.domain.chat.model.ChatItem

sealed interface ChatListScreenAction {
    object SearchClicked : ChatListScreenAction
    object ClearSearch : ChatListScreenAction
    data class SearchQueryChanged(val query: String) : ChatListScreenAction

    data class ItemClick(val chatItem: ChatItem) : ChatListScreenAction
    data object OnRefresh: ChatListScreenAction
}