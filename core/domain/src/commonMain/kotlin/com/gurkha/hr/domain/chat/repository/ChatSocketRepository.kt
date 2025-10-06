package com.gurkha.hr.domain.chat.repository

import com.gurkha.model.chat.Content
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ChatSocketRepository {
    val onConnect: SharedFlow<Unit>
    val onContent: SharedFlow<Content>
    val onTyping: SharedFlow<Unit>
    val onTypingStop: SharedFlow<Unit>
    val isConnected: StateFlow<Boolean>

    suspend fun connect(username: String, chatId: String, socketPrefix: String)
    fun joinRoom(chatId: String, fromUser: String, initiatorId: String)
    fun sendMessage(chatId: String, fromUser: String, message: String)
    fun sendTyping(chatId: String, fromUser: String)
    fun sendStopTyping(chatId: String, fromUser: String)
    fun disconnect()

}