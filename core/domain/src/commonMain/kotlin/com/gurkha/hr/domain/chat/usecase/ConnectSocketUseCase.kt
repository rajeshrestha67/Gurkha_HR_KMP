package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.chat.repository.ChatSocketRepository
import kotlinx.coroutines.flow.firstOrNull

class ConnectSocketUseCase(
    private val chatSocketRepository: ChatSocketRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(chatId: String, socketPrefix: String) {
        val username = userDataRepository.userDataFlow.firstOrNull()?.fullName ?: ""
        chatSocketRepository.connect(
            username = username,
            chatId = chatId,
            socketPrefix = socketPrefix
        )
    }

}