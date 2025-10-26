package com.gurkha.hr.chat_list.model

import com.gurkha.hr.domain.chat.model.ChatItem

data class ChatListScreenState(
    val isLoading: Boolean = false,
    val chatList: List<ChatItem> = emptyList(),
    val chatListCache: List<ChatItem> = emptyList(),
    val query: String? = null,
    val error: String? = null,
    val showSearch: Boolean = false
)
