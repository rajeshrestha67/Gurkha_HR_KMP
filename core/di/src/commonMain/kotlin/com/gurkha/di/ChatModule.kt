package com.gurkha.di

import com.gurkha.hr.chat_list.ChatListViewModel
import com.gurkha.hr.chat_room.ChatRoomViewModel
import com.gurkha.hr.data.chat.KtorChatRemoteRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.repository.ChatRemoteRepository
import com.gurkha.hr.domain.chat.usecase.ChatListUseCase
import com.gurkha.hr.domain.chat.usecase.FetchChatMessageUseCase
import com.gurkha.hr.network.WebSocketManager
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class ChatModule {
    @Factory(binds = [ChatRemoteRepository::class])
    fun getChatRemoteRepository(httpClient: HttpClient) = KtorChatRemoteRepository(httpClient)

    @Factory
    fun getChatListUseCase(chatRemoteRepository: ChatRemoteRepository) =
        ChatListUseCase(chatRemoteRepository)

    @Factory
    fun fetchChatMessageUseCase(
        chatRemoteRepository: ChatRemoteRepository,
        userDataRepository: UserDataRepository
    ) = FetchChatMessageUseCase(
        chatRemoteRepository = chatRemoteRepository,
        userDataRepository = userDataRepository
    )

    @Factory
    fun getWebsocket(httpClient: HttpClient): WebSocketManager =
        WebSocketManager(client = httpClient)

    @KoinViewModel
    fun getChatListViewModel(chatListUseCase: ChatListUseCase) = ChatListViewModel(chatListUseCase)

    @KoinViewModel
    fun getChatRoomViewModel(fetchChatMessageUseCase: FetchChatMessageUseCase) =
        ChatRoomViewModel(fetchChatMessageUseCase = fetchChatMessageUseCase)
}