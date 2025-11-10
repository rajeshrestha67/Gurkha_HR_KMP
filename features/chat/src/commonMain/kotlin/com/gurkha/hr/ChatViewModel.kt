package com.gurkha.hr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.mapper.getCurrentDataAndTime
import com.gurkha.hr.domain.chat.usecase.ChatEmployListUseCase
import com.gurkha.hr.domain.chat.usecase.ConnectSocketUseCase
import com.gurkha.hr.domain.chat.usecase.DisconnectSocketUseCase
import com.gurkha.hr.domain.chat.usecase.FetchChatMessageUseCase
import com.gurkha.hr.domain.chat.usecase.JoinRoomUseCase
import com.gurkha.hr.domain.chat.usecase.ObserveSocketEventsUseCase
import com.gurkha.hr.domain.chat.usecase.SendMessageUseCase
import com.gurkha.hr.domain.chat.usecase.SendStopTypingUseCase
import com.gurkha.hr.domain.chat.usecase.SendTypingUseCase
import com.gurkha.hr.model.ChatMessage
import com.gurkha.hr.model.ChatScreenAction
import com.gurkha.hr.model.ChatScreenState
import com.gurkha.hr.model.toChatMetaData
import com.gurkha.hr.model.toMessage
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.chat.ChatUserData
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

class ChatViewModel(
    private val chatEmployListUseCase: ChatEmployListUseCase,
    private val fetchChatMessageUseCase: FetchChatMessageUseCase,
    private val connectSocketUseCase: ConnectSocketUseCase,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val sendTypingUseCase: SendTypingUseCase,
    private val sendStopTypingUseCase: SendStopTypingUseCase,
    private val observeSocketEventsUseCase: ObserveSocketEventsUseCase,
    private val disconnectSocketUseCase: DisconnectSocketUseCase,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ChatScreenState())
    private var typingJob: Job? = null
    val state = _state.onStart {
        fetchEmployList()
        connectSocketUseCase()
        observeMessage()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatScreenState()
    )

    private val _navigateToChatChannel = Channel<String>()
    val navigateToChatChannel = _navigateToChatChannel.receiveAsFlow()

    fun onAction(action: ChatScreenAction) {
        when (action) {
            ChatScreenAction.ClearSearch -> {
                _state.update {
                    it.copy(
                        showSearch = false,
                        query = null,
                        chatList = it.chatListCache
                    )
                }
            }

            ChatScreenAction.SearchClicked -> {
                _state.update {
                    it.copy(
                        showSearch = true
                    )
                }
            }

            is ChatScreenAction.SearchQueryChanged -> {
                updateChatList(query = action.query)
            }

            is ChatScreenAction.OnEmployeeRefresh -> {
                fetchEmployList(isRefreshing = true)
            }

            is ChatScreenAction.MessageChanged -> {
                _state.update {
                    it.copy(
                        message = action.message
                    )
                }
                typingJob?.cancel()
                if (action.message.isNotEmpty()) {
                    sendTyping(true)
                }

                typingJob = viewModelScope.launch {
                    delay(600)
                    sendTyping(false)
                }
            }

            is ChatScreenAction.Send -> {
                if (state.value.message.isNotEmpty()) {
                    sendMessage()
                }
            }

            ChatScreenAction.OnChatRefresh -> {
                _state.value.chatUserData?.let { chatUserData ->
                    fetchChatMessage(chatUserData = chatUserData, isRefreshing = true)
                }
            }

            is ChatScreenAction.ItemClick -> {
                val chatUserData = ChatUserData(
                    employeeId = action.chatItem.employeeId,
                    chatId = action.chatItem.chatId,
                    branchName = action.chatItem.branchName,
                    employeeName = action.chatItem.employeeName,
                    profileImageUrl = action.chatItem.profileImageUrl,
                    nameInitials = action.chatItem.nameInitials,
                    backgroundColor = action.chatItem.backgroundColor.value,
                    phoneNumber = action.chatItem.phoneNumber
                )
                viewModelScope.launch {
                    _navigateToChatChannel.send(Json.encodeToString(chatUserData))
                }
            }

            is ChatScreenAction.UpdateCurrentEmploy -> {
                val userData = Json.decodeFromString<ChatUserData>(action.json)
                _state.update {
                    it.copy(
                        chatUserData = userData
                    )
                }
                fetchChatMessage(chatUserData = userData)
                joinChatRoom(chatUserData = userData)
            }
        }
    }

    private fun observeMessage() {
        viewModelScope.launch {
            observeSocketEventsUseCase.onContent.collect { content ->
                content?.let {

                    val chatMessage = createChatMessage(
                        fromMe = content.fromUser == userDataRepository.userDataFlow.firstOrNull()!!.fullName,
                        message = content.content
                    )
                    _state.update {
                        it.copy(
                            messages = state.value.messages.addMessage(chatMessage)
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            observeSocketEventsUseCase.onTyping.collect { typing ->
                _state.update {
                    it.copy(
                        isTyping = typing
                    )
                }
            }
        }
        viewModelScope.launch {
            observeSocketEventsUseCase.onTyping.collect { typing ->
                _state.update {
                    it.copy(
                        isTyping = typing
                    )
                }
            }
        }
        viewModelScope.launch {
            observeSocketEventsUseCase.onUserStatusChange.collect { status ->
                status?.let { userStatus ->
                    _state.update { current ->
                        val updatedData = state.value.chatListCache.filter { chatItem ->
                            chatItem.employeeName.contains(
                                other = state.value.query ?: "",
                                ignoreCase = true
                            )
                        }.map { item ->
                            if (item.chatId == status.chatId) {
                                item.copy(isOnline = status.status)
                            } else {
                                item
                            }
                        }
                        current.copy(
                            chatList = updatedData,
                            chatListCache = updatedData
                        )
                    }
                }
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

    private fun fetchEmployList(isRefreshing: Boolean = false) = viewModelScope.launch {
        if (isRefreshing) {
            _state.update {
                it.copy(isEmployListRefreshing = true)
            }
        } else {
            _state.update {
                it.copy(isEmployListLoading = true)
            }
        }
        chatEmployListUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isEmployListLoading = false,
                    isEmployListRefreshing = false,
                    chatList = data,
                    chatListCache = data
                )
            }
        }.onError { error ->
            _state.update {
                it.copy(
                    isEmployListLoading = false,
                    isEmployListRefreshing = false,
                    error = error.toErrorMessage()
                )
            }
        }
    }

    private fun sendTyping(isTyping: Boolean) = viewModelScope.launch {
        if (isTyping) {
            sendTypingUseCase(chatId = state.value.chatUserData?.chatId ?: "")
        } else {
            sendStopTypingUseCase(chatId = state.value.chatUserData?.chatId ?: "")
        }

    }

    @OptIn(ExperimentalTime::class)
    private fun sendMessage() = viewModelScope.launch {
        sendTyping(isTyping = false)
        val message = state.value.message

        _state.update {
            it.copy(
                message = ""
            )
        }
        sendMessageUseCase(
            chatId = state.value.chatUserData?.chatId ?: "",
            message = message
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

    private fun joinChatRoom(chatUserData: ChatUserData) {
        viewModelScope.launch {
            joinRoomUseCase(
                chatId = chatUserData.chatId,
                initiatorId = "app_mbank"
            )
        }
        viewModelScope.launch {
            observeSocketEventsUseCase(chatId = chatUserData.chatId)
        }
    }

    private fun fetchChatMessage(
        chatUserData: ChatUserData,
        isRefreshing: Boolean = false
    ) = viewModelScope.launch {

        if (isRefreshing) {
            _state.update {
                it.copy(
                    isChatRefreshing = true
                )
            }
        } else {
            _state.update {
                it.copy(
                    isChatLoading = true
                )
            }
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

//                        .asReversed()
                        .toMap(LinkedHashMap()),
                    metaData = data.metaData?.toChatMetaData(),
                    isChatLoading = false,
                    isChatRefreshing = false
                )
            }

        }.onError {
            _state.update {
                it.copy(isChatLoading = false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnectSocketUseCase()
    }

}