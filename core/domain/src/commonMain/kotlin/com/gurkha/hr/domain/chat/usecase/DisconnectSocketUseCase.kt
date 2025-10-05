package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.domain.chat.repository.ChatSocketRepository

class DisconnectSocketUseCase(private val chatSocketRepository: ChatSocketRepository) {
    operator fun invoke() = chatSocketRepository.disconnect()
}