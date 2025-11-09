package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.repository.ChatSocketRepository
import kotlinx.coroutines.flow.firstOrNull

class ObserveSocketEventsUseCase(
    private val chatSocketRepository: ChatSocketRepository,
    private val userDataRepository: UserDataRepository
) {
    val onContent = chatSocketRepository.onContent
    val onUserStatusChange = chatSocketRepository.onUserStatusChanged
    val onTyping = chatSocketRepository.onTyping

    //    val onTypingStop = chatSocketRepository.onTypingStop
    val isConnected = chatSocketRepository.isConnected

    suspend operator fun invoke(chatId: String) {
        val username = userDataRepository.userDataFlow.firstOrNull()?.fullName ?: ""
        chatSocketRepository.observeChange(username = username, chatId = chatId)
    }
}