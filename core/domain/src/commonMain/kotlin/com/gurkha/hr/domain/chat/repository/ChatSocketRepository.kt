package com.gurkha.hr.domain.chat.repository

import com.gurkha.model.chat.Content
import kotlinx.coroutines.flow.StateFlow

interface ChatSocketRepository {

    val onContent: StateFlow<Content?>
    val onTyping: StateFlow<Boolean>

    //    val onTypingStop: StateFlow<Unit>
    val isConnected: StateFlow<Boolean>

    suspend fun connect(username: String, chatId: String, socketPrefix: String)
    fun joinRoom(chatId: String, fromUser: String, initiatorId: String)
    fun sendMessage(chatId: String, fromUser: String, message: String)
    fun sendTyping(chatId: String, fromUser: String)
    fun sendStopTyping(chatId: String, fromUser: String)
    fun disconnect()

}