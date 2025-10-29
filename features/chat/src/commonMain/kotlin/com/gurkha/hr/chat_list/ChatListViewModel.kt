package com.gurkha.hr.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.chat_list.model.ChatListScreenAction
import com.gurkha.hr.chat_list.model.ChatListScreenState
import com.gurkha.hr.domain.chat.usecase.ChatListUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.chat.ChatUserData
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ChatListViewModel(
    private val chatListUseCase: ChatListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChatListScreenState())

    val state = _state.onStart {
        fetchChatList()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatListScreenState()
    )

    private val _navigateChannel = Channel<String?>()
    val navigateChannel = _navigateChannel.receiveAsFlow()

    fun onAction(action: ChatListScreenAction) {
        when (action) {
            ChatListScreenAction.ClearSearch -> {
                _state.update {
                    it.copy(
                        showSearch = false,
                        query = null,
                        chatList = it.chatListCache
                    )
                }
            }

            ChatListScreenAction.SearchClicked -> {
                _state.update {
                    it.copy(
                        showSearch = true
                    )
                }
            }

            is ChatListScreenAction.SearchQueryChanged -> {
                updateChatList(query = action.query)
            }

            is ChatListScreenAction.ItemClick -> {
                val chatUserData = ChatUserData(
                    employeeId = action.chatItem.employeeId,
                    chatId = action.chatItem.chatId,
                    branchName = action.chatItem.branchName,
                    employeeName = action.chatItem.employeeName,
                    profileImageUrl = action.chatItem.profileImageUrl,
                    nameInitials = action.chatItem.nameInitials,
                    backgroundColor = action.chatItem.backgroundColor.value
                )
                _navigateChannel.trySend(
                    Json.encodeToString(chatUserData)
                )
            }

            is ChatListScreenAction.OnRefresh -> {
                refresh()
            }
        }
    }

    private fun updateChatList(query: String?) {
        _state.update {
            it.copy(
                query = query,
                chatList = it.chatListCache.filter { chatItem ->
                    chatItem.employeeName.contains(query ?: "", ignoreCase = true)
                }
            )
        }
    }

    private fun fetchChatList() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        chatListUseCase().onSuccess { data ->
            _state.update {
                it.copy(isLoading = false, chatList = data, chatListCache = data)
            }
        }.onError { error ->
            _state.update {
                it.copy(isLoading = false, error = error.toErrorMessage())
            }
        }
    }

    private fun refresh()=viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = true
            )
        }
        fetchChatList()
        _state.update {
            it.copy(
                isRefreshing = false
            )
        }
    }
}