package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.repository.ChatSocketRepository
import kotlinx.coroutines.flow.firstOrNull

class SendTypingUseCase(
    private val chatSocketRepository: ChatSocketRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(isTyping: Boolean, chatId: String) {
        val fromUser = userDataRepository.userDataFlow.firstOrNull()?.fullName ?: ""
        chatSocketRepository.sendTyping(
            isTyping = isTyping,
            chatId = chatId,
            fromUser = fromUser
        )
    }
}