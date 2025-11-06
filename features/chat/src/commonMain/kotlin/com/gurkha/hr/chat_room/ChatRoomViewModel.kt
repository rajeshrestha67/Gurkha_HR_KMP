package com.gurkha.hr.chat_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.chat_room.model.ChatMessage
import com.gurkha.hr.chat_room.model.ChatRoomScreenAction
import com.gurkha.hr.chat_room.model.ChatRoomScreenState
import com.gurkha.hr.chat_room.model.toChatMetaData
import com.gurkha.hr.chat_room.model.toMessage
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.mapper.getCurrentDataAndTime
import com.gurkha.hr.domain.chat.usecase.FetchChatMessageUseCase
import com.gurkha.hr.network.SocketManager
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.chat.ChatUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

class ChatRoomViewModel(
    private val fetchChatMessageUseCase: FetchChatMessageUseCase,
//    private val connectSocketUseCase: ConnectSocketUseCase,
//    private val joinRoomUseCase: JoinRoomUseCase,
//    private val sendMessageUseCase: SendMessageUseCase,
//    private val sendTypingUseCase: SendTypingUseCase,
//    private val sendStopTypingUseCase: SendStopTypingUseCase,
//    private val observeSocketEventsUseCase: ObserveSocketEventsUseCase,
//    private val disconnectSocketUseCase: DisconnectSocketUseCase
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val socketManager: SocketManager = SocketManager()
    private val _state = MutableStateFlow(ChatRoomScreenState())

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChatRoomScreenState()
        )

    init {
        viewModelScope.launch {


        }
    }

    override fun onCleared() {
        super.onCleared()
//        disconnectSocketUseCase()
    }

    fun onAction(action: ChatRoomScreenAction) {
        when (action) {

            is ChatRoomScreenAction.UpdateChatData -> {
                val chatUserData = Json.decodeFromString<ChatUserData>(string = action.json)
                _state.update {
                    it.copy(chatUserData = chatUserData)
                }
                initSocket(chatUserData = chatUserData)
                fetchChatMessage(chatUserData = chatUserData)
            }

            is ChatRoomScreenAction.MessageChanged -> {
                _state.update {
                    it.copy(
                        message = action.message
                    )
                }
            }

            is ChatRoomScreenAction.OnTyping -> {
                sendTyping(isTyping = action.isTyping)
            }

            is ChatRoomScreenAction.Send -> {
                if (state.value.message.isNotEmpty()) {
                    sendMessage()
                }
            }

            ChatRoomScreenAction.OnRefresh -> {
                refresh()
            }
        }
    }

    private fun sendTyping(isTyping: Boolean) = viewModelScope.launch {
//        _state.update {
//            it.copy(isUserTyping = isTyping)
//        }
        if (isTyping) {
            //sendTypingUseCase(chatId = state.value.chatUserData?.chatId ?: "")
        } else {
            //sendStopTypingUseCase(chatId = state.value.chatUserData?.chatId ?: "")
        }

    }

    @OptIn(ExperimentalTime::class)
    private fun sendMessage() = viewModelScope.launch {
        sendTyping(isTyping = false)
        val message = state.value.message
        val chatMessage = createChatMessage(
            fromMe = true,
            message = message
        )
        _state.update {
            it.copy(
                messages = state.value.messages.addMessage(chatMessage),
                message = ""
            )
        }
//        sendMessageUseCase(
//            chatId = state.value.chatUserData?.chatId ?: "",
//            message = message
//        )

        println("fullname ${userDataRepository.userDataFlow.firstOrNull()!!.fullName}")
        socketManager.sendMessage(
            chatId = state.value.chatUserData?.chatId ?: "",
            message = message,
            fromUser = userDataRepository.userDataFlow.firstOrNull()!!.fullName
        )

    }

    private fun createChatMessage(fromMe: Boolean, message: String?): ChatMessage? {
        return message?.let {
            val nowPair = getCurrentDataAndTime()
            ChatMessage(
                message = message,
                date = nowPair.first,
                time = nowPair.second,
                fromMe = fromMe
            )
        }
    }

    private fun LinkedHashMap<String, List<ChatMessage>>.addMessage(
        chatMessage: ChatMessage?
    ): LinkedHashMap<String, List<ChatMessage>> {
        if (chatMessage == null) return this
        val dateKey = chatMessage.date
        return LinkedHashMap(this).apply {
            this[dateKey] = (this[dateKey] ?: emptyList()) + chatMessage
        }
    }

    /*
    private fun LinkedHashMap<String, List<ChatMessage>>.addMessage(chatMessage: ChatMessage): LinkedHashMap<String, List<ChatMessage>> { val dateKey = chatMessage.date return LinkedHashMap(this).apply { this[dateKey] = listOf(chatMessage) + (this[dateKey] ?: emptyList()) } }
     */
    private fun initSocket(chatUserData: ChatUserData) {
        viewModelScope.launch {
//            connectSocketUseCase(
//                chatId = chatUserData.chatId,
//                socketPrefix = "mbank"
//            )
            socketManager.connect(
                username = userDataRepository.userDataFlow.firstOrNull()!!.fullName,
                chatId = chatUserData.chatId,
                socketPrefix = "mbank"
            )
        }
        viewModelScope.launch {
            socketManager.isConnected.collect {
                if (it) {
                    socketManager.joinRoom(
                        chatId = chatUserData.chatId,
                        initiatorId = "app_mbank",
                        fromUser = userDataRepository.userDataFlow.firstOrNull()!!.fullName
                    )
                }
            }
        }
        viewModelScope.launch {
            socketManager.onTyping.collect { typing ->
                _state.update {
                    it.copy(
                        isTyping = typing
                    )
                }
            }
        }
        viewModelScope.launch {
            socketManager.onTyping.collect { typing ->
                println("ChatRoomViewModel typing stop")
                _state.update {
                    it.copy(
                        isTyping = typing
                    )
                }
            }
        }
        viewModelScope.launch {
            socketManager.onContent.collect { content ->
                val chatMessage = createChatMessage(
                    fromMe = false,
                    message = content?.content
                )
                _state.update {
                    it.copy(
                        messages = state.value.messages.addMessage(chatMessage)
                    )
                }
            }
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
            _state.update {
                it.copy(
                    messages = data.messages
                        .map { chat ->
                            chat.toMessage()
                        }
                        .groupByTo(LinkedHashMap()) { chatData ->
                            chatData.date
                        }
//                        .mapValues { entry ->
//                            entry.value.reversed()
//                        }
                        .toList()
//                        .asReversed()
                        .toMap(LinkedHashMap()),
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

    private fun refresh() = viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = true
            )
        }
        _state.update {
            it.copy(
                isRefreshing = false
            )
        }
    }

}