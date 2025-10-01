package com.gurkha.di

import com.gurkha.hr.chat_list.ChatListViewModel
import com.gurkha.hr.chat_room.ChatRoomViewModel
import com.gurkha.hr.data.chat.KtorChatRemoteRepository
import com.gurkha.hr.domain.chat.repository.ChatRemoteRepository
import com.gurkha.hr.domain.chat.usecase.ChatListUseCase
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

    @KoinViewModel
    fun getChatListViewModel(chatListUseCase: ChatListUseCase) = ChatListViewModel(chatListUseCase)

    @KoinViewModel
    fun getChatRoomViewModel() = ChatRoomViewModel()
}