package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.datastore.user_data.model.UserData
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.mapper.toChatMessageData
import com.gurkha.hr.domain.chat.model.ChatMessageData
import com.gurkha.hr.domain.chat.repository.ChatRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import kotlinx.coroutines.flow.firstOrNull

class FetchChatMessageUseCase(
    private val chatRemoteRepository: ChatRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(
        chatId: String,
        page: Int,
        size: Int
    ): ERPResult<ChatMessageData, DataError> {
        val userData = userDataRepository.userDataFlow.firstOrNull() ?: UserData()
        return chatRemoteRepository.fetchChatMessage(
            chatId = chatId,
            page = page,
            size = size
        ).map { chatMessageResponseDto ->
            chatMessageResponseDto.toChatMessageData(userData)
        }
    }
}