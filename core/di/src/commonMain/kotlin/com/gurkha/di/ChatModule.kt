package com.gurkha.di

import com.gurkha.hr.ChatViewModel
import com.gurkha.hr.data.chat.IOChatSocketRepository
import com.gurkha.hr.data.chat.KtorChatRemoteRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.repository.ChatRemoteRepository
import com.gurkha.hr.domain.chat.repository.ChatSocketRepository
import com.gurkha.hr.domain.chat.usecase.ChatEmployListUseCase
import com.gurkha.hr.domain.chat.usecase.ConnectSocketUseCase
import com.gurkha.hr.domain.chat.usecase.DisconnectSocketUseCase
import com.gurkha.hr.domain.chat.usecase.FetchChatMessageUseCase
import com.gurkha.hr.domain.chat.usecase.JoinRoomUseCase
import com.gurkha.hr.domain.chat.usecase.ObserveSocketEventsUseCase
import com.gurkha.hr.domain.chat.usecase.SendMessageUseCase
import com.gurkha.hr.domain.chat.usecase.SendStopTypingUseCase
import com.gurkha.hr.domain.chat.usecase.SendTypingUseCase
import com.gurkha.hr.network.SocketManager
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ChatModule {
    @Factory(binds = [ChatRemoteRepository::class])
    fun getChatRemoteRepository(httpClient: HttpClient) = KtorChatRemoteRepository(httpClient)

    @Single
    fun getSocketManager() = SocketManager()

    @Single(binds = [ChatSocketRepository::class])
    fun getChatSocketRepository(socketManager: SocketManager) =
        IOChatSocketRepository(socketManager)

    @Factory
    fun getChatListUseCase(chatRemoteRepository: ChatRemoteRepository) =
        ChatEmployListUseCase(chatRemoteRepository)

    @Factory
    fun fetchChatMessageUseCase(
        chatRemoteRepository: ChatRemoteRepository,
        userDataRepository: UserDataRepository
    ) = FetchChatMessageUseCase(
        chatRemoteRepository = chatRemoteRepository,
        userDataRepository = userDataRepository
    )


    @Single
    fun getConnectSocketUseCase(
        chatSocketRepository: ChatSocketRepository,
        userDataRepository: UserDataRepository
    ) = ConnectSocketUseCase(
        chatSocketRepository = chatSocketRepository,
        userDataRepository = userDataRepository
    )

    @Single
    fun getJoinRoomUseCase(
        chatSocketRepository: ChatSocketRepository,
        userDataRepository: UserDataRepository
    ) = JoinRoomUseCase(
        chatSocketRepository = chatSocketRepository,
        userDataRepository = userDataRepository
    )

    @Single
    fun getSendMessageUseCase(
        chatSocketRepository: ChatSocketRepository,
        userDataRepository: UserDataRepository
    ) = SendMessageUseCase(
        chatSocketRepository = chatSocketRepository,
        userDataRepository = userDataRepository
    )

    @Single
    fun getSendTypingUseCase(
        chatSocketRepository: ChatSocketRepository,
        userDataRepository: UserDataRepository
    ) = SendTypingUseCase(
        chatSocketRepository = chatSocketRepository,
        userDataRepository = userDataRepository
    )

    @Single
    fun getSendStopTypingUseCase(
        chatSocketRepository: ChatSocketRepository,
        userDataRepository: UserDataRepository
    ) = SendStopTypingUseCase(
        chatSocketRepository = chatSocketRepository,
        userDataRepository = userDataRepository
    )

    @Single
    fun getObserveSocketEventsUseCase(chatSocketRepository: ChatSocketRepository) =
        ObserveSocketEventsUseCase(chatSocketRepository)

    @Single
    fun getDisconnectSocketUseCase(chatSocketRepository: ChatSocketRepository) =
        DisconnectSocketUseCase(chatSocketRepository)


//    @KoinViewModel
//    fun getChatListViewModel(chatListUseCase: ChatEmployListUseCase) =
//        ChatListViewModel(chatListUseCase)

    @KoinViewModel
    fun getChatViewModel(
        fetchChatMessageUseCase: FetchChatMessageUseCase,
        userDataRepository: UserDataRepository,
        chatEmployListUseCase: ChatEmployListUseCase
    ): ChatViewModel = ChatViewModel(
        chatEmployListUseCase = chatEmployListUseCase,
        fetchChatMessageUseCase = fetchChatMessageUseCase,
        userDataRepository = userDataRepository
    )

//    @KoinViewModel
//    fun getChatRoomViewModel(
//        fetchChatMessageUseCase: FetchChatMessageUseCase,
//        connectSocketUseCase: ConnectSocketUseCase,
//        joinRoomUseCase: JoinRoomUseCase,
//        sendMessageUseCase: SendMessageUseCase,
//        sendTypingUseCase: SendTypingUseCase,
//        sendStopTypingUseCase: SendStopTypingUseCase,
//        observeSocketEventsUseCase: ObserveSocketEventsUseCase,
//        disconnectSocketUseCase: DisconnectSocketUseCase,
//        userDataRepository: UserDataRepository
//    ) = ChatRoomViewModel(
//        fetchChatMessageUseCase = fetchChatMessageUseCase,
////        connectSocketUseCase = connectSocketUseCase,
////        joinRoomUseCase = joinRoomUseCase,
////        sendMessageUseCase = sendMessageUseCase,
////        sendTypingUseCase = sendTypingUseCase,
////        sendStopTypingUseCase = sendStopTypingUseCase,
////        observeSocketEventsUseCase = observeSocketEventsUseCase,
////        disconnectSocketUseCase = disconnectSocketUseCase
//        userDataRepository = userDataRepository
//    )
}