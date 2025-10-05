package com.gurkha.hr.domain.chat.repository

import com.gurkha.model.chat.Content
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ChatSocketRepository {
    val onConnect: SharedFlow<Unit>
    val onContent: SharedFlow<Content>
    val onTyping: SharedFlow<Boolean>
    val isConnected: StateFlow<Boolean>

    suspend fun connect(username: String, chatId: String, socketPrefix: String)
    fun joinRoom(chatId: String, fromUser: String, initiatorId: String)
    fun sendMessage(chatId: String, fromUser: String, message: String)
    fun sendTyping(isTyping: Boolean, chatId: String, fromUser: String)
    fun disconnect()

}