package com.gurkha.hr.chat_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.chat_room.model.ChatRoomScreenAction
import com.gurkha.hr.chat_room.model.ChatRoomScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class ChatRoomViewModel : ViewModel() {

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
                _state.update {
                    it.copy(chatUserData = Json.decodeFromString(action.json))
                }
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
                println("sent")
            }
        }
    }

}