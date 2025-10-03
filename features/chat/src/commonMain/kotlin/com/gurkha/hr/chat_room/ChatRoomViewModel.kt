package com.gurkha.hr.chat_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.chat_room.model.ChatMessage
import com.gurkha.hr.chat_room.model.ChatRoomScreenAction
import com.gurkha.hr.chat_room.model.ChatRoomScreenState
import com.gurkha.hr.chat_room.model.toChatMetaData
import com.gurkha.hr.chat_room.model.toMessage
import com.gurkha.hr.domain.chat.mapper.getCurrentDataAndTime
import com.gurkha.hr.domain.chat.usecase.FetchChatMessageUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.chat.ChatUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

class ChatRoomViewModel(
    private val fetchChatMessageUseCase: FetchChatMessageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChatRoomScreenState())

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChatRoomScreenState()
        )

    fun onAction(action: ChatRoomScreenAction) {
        when (action) {

            is ChatRoomScreenAction.UpdateChatData -> {
                val chatUserData = Json.decodeFromString<ChatUserData>(string = action.json)
                _state.update {
                    it.copy(chatUserData = chatUserData)
                }
                fetchChatMessage(chatUserData = chatUserData)
            }

            ChatRoomScreenAction.ClearSearch -> {
                _state.update {
                    it.copy(
                        message = ""
                    )
                }
            }

            is ChatRoomScreenAction.SearchQueryChanged -> {
                _state.update {
                    it.copy(
                        message = action.message
                    )
                }
            }

            is ChatRoomScreenAction.Send -> {
                sendMessage()
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun sendMessage() = viewModelScope.launch {
        val messages = state.value.messages
        val nowPair = getCurrentDataAndTime()
        val chatMessage = ChatMessage(
            message = state.value.message,
            date = nowPair.first,
            time = nowPair.second,
            fromMe = true
        )
        _state.update {
            it.copy(
                messages = messages.addMessage(chatMessage),
                message = ""
            )
        }
    }

    private fun LinkedHashMap<String, List<ChatMessage>>.addMessage(chatMessage: ChatMessage): LinkedHashMap<String, List<ChatMessage>> {
        val dateKey = chatMessage.date
        return LinkedHashMap(this).apply {
            this[dateKey] = listOf(chatMessage) + (this[dateKey] ?: emptyList())
        }
    }

    private fun fetchChatMessage(chatUserData: ChatUserData) = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        fetchChatMessageUseCase(
            chatId = chatUserData.chatId,
            page = 0,
            size = 50
        ).onSuccess { data ->

            data.messages.forEach {
                println("message for $it")
            }
            _state.update {
                it.copy(
                    messages = data.messages
                        .map {
                            val a = it.toMessage()
                            println("message $a")
                            a
                        }.groupByTo(LinkedHashMap()) { chatData ->
                            chatData.date
                        }.mapValues { entry -> entry.value.reversed() }
                        .let { LinkedHashMap(it) },
                    metaData = data.metaData?.toChatMetaData(),
                    isLoading = false
                )
            }

        }.onError {
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

}