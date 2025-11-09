package com.gurkha.hr.domain.chat.repository

import com.gurkha.model.chat.ContentData
import com.gurkha.model.chat.UserStatusChangeData
import kotlinx.coroutines.flow.StateFlow

interface ChatSocketRepository {

    val onContent: StateFlow<ContentData?>
    val onUserStatusChanged: StateFlow<UserStatusChangeData?>
    val onTyping: StateFlow<Boolean>

    //    val onTypingStop: StateFlow<Unit>
    val isConnected: StateFlow<Boolean>

    fun connect()
    fun observeChange(username: String, chatId: String)
    fun joinRoom(chatId: String, fromUser: String, initiatorId: String)
    fun sendMessage(chatId: String, fromUser: String, message: String)
    fun sendTyping(chatId: String, fromUser: String)
    fun sendStopTyping(chatId: String, fromUser: String)
    fun disconnect()

}