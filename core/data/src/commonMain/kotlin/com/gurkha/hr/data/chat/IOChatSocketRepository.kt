package com.gurkha.hr.data.chat

import com.gurkha.hr.domain.chat.repository.ChatSocketRepository
import com.gurkha.hr.network.SocketManager

class IOChatSocketRepository(
    private val socketManager: SocketManager
) : ChatSocketRepository {
    override val onConnect = socketManager.onConnect
    override val onContent = socketManager.onContent
    override val onTyping = socketManager.onTyping
    override val isConnected = socketManager.isConnected

    override suspend fun connect(username: String, chatId: String, socketPrefix: String) =
        socketManager.connect(username, chatId, socketPrefix)

    override fun joinRoom(chatId: String, fromUser: String, initiatorId: String) =
        socketManager.joinRoom(chatId, fromUser, initiatorId)

    override fun sendMessage(chatId: String, fromUser: String, message: String) =
        socketManager.sendMessage(chatId, fromUser, message)

    override fun disconnect() {
        socketManager.disconnect()
    }
}