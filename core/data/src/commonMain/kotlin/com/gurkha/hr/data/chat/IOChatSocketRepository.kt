package com.gurkha.hr.data.chat

import com.gurkha.hr.domain.chat.repository.ChatSocketRepository
import com.gurkha.hr.network.SocketManager

class IOChatSocketRepository(
    private val socketManager: SocketManager
) : ChatSocketRepository {

    override val onContent = socketManager.onContent
    override val onUserStatusChanged = socketManager.onUserStatusChange
    override val onTyping = socketManager.onTyping
    override val isConnected = socketManager.isConnected

    override fun connect() = socketManager.connect()

    override fun observeChange(username: String, chatId: String) =
        socketManager.observeChange(username, chatId)

    override fun joinRoom(chatId: String, fromUser: String, initiatorId: String) =
        socketManager.joinRoom(chatId, fromUser, initiatorId)

    override fun sendMessage(chatId: String, fromUser: String, message: String) =
        socketManager.sendMessage(chatId, fromUser, message)

    override fun sendTyping(chatId: String, fromUser: String) {
        socketManager.sendStartTyping(chatId, fromUser)
    }

    override fun sendStopTyping(chatId: String, fromUser: String) {
        socketManager.sendStopTyping(chatId, fromUser)
    }

    override fun disconnect() {
        socketManager.disconnect()
    }
}