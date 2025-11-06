package com.gurkha.hr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.mapper.getCurrentDataAndTime
import com.gurkha.hr.domain.chat.usecase.ChatEmployListUseCase
import com.gurkha.hr.domain.chat.usecase.FetchChatMessageUseCase
import com.gurkha.hr.model.ChatMessage
import com.gurkha.hr.model.ChatScreenAction
import com.gurkha.hr.model.ChatScreenState
import com.gurkha.hr.model.toChatMetaData
import com.gurkha.hr.model.toMessage
import com.gurkha.hr.network.SocketManager
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.chat.ChatUserData
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class ChatViewModel(
    private val chatEmployListUseCase: ChatEmployListUseCase,
    private val fetchChatMessageUseCase: FetchChatMessageUseCase,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ChatScreenState())

    val state = _state.onStart {
        fetchChatList()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatScreenState()
    )
    private val socketManager: SocketManager = SocketManager()
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

//            is ChatScreenAction.ItemClick -> {
//
//                val chatUserData = ChatUserData(
//                    employeeId = action.chatItem.employeeId,
//                    chatId = action.chatItem.chatId,
//                    branchName = action.chatItem.branchName,
//                    employeeName = action.chatItem.employeeName,
//                    profileImageUrl = action.chatItem.profileImageUrl,
//                    nameInitials = action.chatItem.nameInitials,
//                    backgroundColor = action.chatItem.backgroundColor.value,
//                    phoneNumber = action.chatItem.phoneNumber
//                )
//            }

            is ChatScreenAction.OnEmployeeRefresh -> {
                fetchChatList(isRefreshing = true)
            }

            is ChatScreenAction.MessageChanged -> {
                _state.update {
                    it.copy(
                        message = action.message
                    )
                }
            }

            is ChatScreenAction.OnTyping -> {
                sendTyping(isTyping = action.isTyping)
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
                _state.update {
                    it.copy(
                        chatUserData = chatUserData
                    )
                }
                fetchChatMessage(chatUserData = chatUserData)
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

    private fun fetchChatList(isRefreshing: Boolean = false) = viewModelScope.launch {
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

    private fun fetchChatMessage(
        chatUserData: ChatUserData,
        isRefreshing: Boolean = false
    ) = viewModelScope.launch {

        if (isRefreshing) {
            _state.update {
                it.copy(
                    isChatLoading = true
                )
            }
        } else {
            _state.update {
                it.copy(
                    isChatRefreshing = true
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
                        .toList()
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

}