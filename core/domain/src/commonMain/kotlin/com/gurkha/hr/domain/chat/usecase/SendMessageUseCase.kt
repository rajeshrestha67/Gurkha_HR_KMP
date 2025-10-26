package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.repository.ChatSocketRepository
import kotlinx.coroutines.flow.firstOrNull

class SendMessageUseCase(
    private val chatSocketRepository: ChatSocketRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(chatId: String, message: String) {
        val fromUser = userDataRepository.userDataFlow.firstOrNull()?.fullName ?: ""
        chatSocketRepository.sendMessage(
            chatId = chatId,
            fromUser = fromUser,
            message = message
        )
    }
}