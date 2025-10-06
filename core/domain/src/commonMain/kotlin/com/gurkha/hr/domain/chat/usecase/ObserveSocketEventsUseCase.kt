package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.domain.chat.repository.ChatSocketRepository

class ObserveSocketEventsUseCase(chatSocketRepository: ChatSocketRepository) {
    val onConnect = chatSocketRepository.onConnect
    val onContent = chatSocketRepository.onContent
    val onTyping = chatSocketRepository.onTyping
    val onTypingStop = chatSocketRepository.onTypingStop
    val isConnected = chatSocketRepository.isConnected
}