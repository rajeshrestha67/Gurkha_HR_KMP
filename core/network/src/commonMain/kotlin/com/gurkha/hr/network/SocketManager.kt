package com.gurkha.hr.network

import com.gurkha.hr.logger.AppLogger
import com.gurkha.model.chat.ContentData
import com.gurkha.model.chat.UserStatusChangeData
import com.piasy.kmp.socketio.socketio.IO
import com.piasy.kmp.socketio.socketio.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SocketManager {
    var socket: Socket? = null

    private val _onContent = MutableStateFlow<ContentData?>(null)
    val onContent: StateFlow<ContentData?> = _onContent

    private val _onUserStatusChange = MutableStateFlow<UserStatusChangeData?>(null)
    val onUserStatusChange: StateFlow<UserStatusChangeData?> = _onUserStatusChange

    private val _onTyping = MutableStateFlow(false)
    val onTyping: StateFlow<Boolean> = _onTyping

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val json = Json {
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = false
        useArrayPolymorphism = false
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    fun connect() {
        if (isConnected.value) {
            return
        }
        val opts = IO.Options().apply {
            path = PATH
            reconnection = true
            reconnectionAttempts = RECONNECTION_ATTEMPTS
            reconnectionDelay = RECONNECTION_DELAY
            transports = TRANSPORTS
            secure = true
            query = mutableMapOf<String, String>().apply {
                //put(SOCKET_PREFIX, "mbank")
            }
        }


        IO.socket(SOCKET_URL, opts) { socket ->
            socket.off(Socket.EVENT_CONNECT)
            socket.off(Socket.EVENT_DISCONNECT)
            socket.off(Socket.EVENT_CONNECT_ERROR)
            socket.off(USER_STATUS_CHANGE)
            socket.off(CHAT_ROOM_USER_LIST)

            socket.on(Socket.EVENT_CONNECT) {
                AppLogger.i(TAG, "socket connected")
                this.socket = socket
                _isConnected.update {
                    true
                }
            }
            socket.on(Socket.EVENT_DISCONNECT) {
                AppLogger.i(TAG, "socket disconnected")
                this.socket = null
                _isConnected.update {
                    false
                }
            }
            socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                AppLogger.i(TAG, "socket connection error ${args.firstOrNull().toString()}")
                this.socket = null
                _isConnected.update {
                    it
                    false
                }
            }
            socket.on(USER_STATUS_CHANGE) { args ->
                args.firstOrNull()?.let { arg ->
                    if (arg is JsonObject) {
                        val userStatusChangeResponseDto =
                            json.decodeFromString<UserStatusChangeData>(string = arg.toString())
                        AppLogger.i(TAG, "socket on user status change $arg")
                        _onUserStatusChange.update {
                            userStatusChangeResponseDto
                        }
                    }
                }
            }
            socket.on(CHAT_ROOM_USER_LIST) { args ->
                println("$TAG ${args.firstOrNull()}")
            }
            socket.open()
        }


    }

    fun observeChange(username: String, chatId: String) {
        switchChatListener(username = username, chatId = chatId)
    }


    private fun switchChatListener(username: String, chatId: String) {
        val s = socket ?: return

        chatId.let { oldId ->
            s.off("$PRIVATE:$oldId")
            s.off("$TYPING:$oldId")
            s.off("$STOP_TYPING:$oldId")
        }

        s.on("$PRIVATE:$chatId") { args ->
            args.firstOrNull()?.let { arg ->
                if (arg is JsonObject) {
                    val contentResponseDto =
                        json.decodeFromString<ContentData>(string = arg.toString())
                    AppLogger.i(TAG, "socket new message $arg")
                    _onContent.update {
                        contentResponseDto
                    }
                }
            }
        }
        s.on("$TYPING:$chatId") { args ->
            args.firstOrNull()?.let { arg ->
                if (arg.toString() != username) {
                    AppLogger.i(TAG, "socket typing")
                    _onTyping.update {
                        true
                    }
                }
            }
        }

        s.on("$STOP_TYPING:$chatId") { args ->
            args.firstOrNull()?.let { arg ->
                if (arg.toString() != username) {
                    AppLogger.i(TAG, "socket stop typing")
                    _onTyping.update {
                        false
                    }
                }
            }
        }

    }


    fun joinRoom(chatId: String, fromUser: String, initiatorId: String) {
        AppLogger.i(TAG, "socket room join for user $fromUser, chat ID: $chatId")
        socket?.emit(JOIN_ROOM, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
            put(INITIATOR_ID, initiatorId)
        })
    }

    fun sendMessage(chatId: String, fromUser: String, message: String) {
        AppLogger.i(TAG, "socket send message from user $fromUser, message: $message")
        socket?.emit(PRIVATE_MESSAGE, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
            put(MESSAGE, message)
        })
    }


    fun sendStartTyping(chatId: String, fromUser: String) {
        AppLogger.i(TAG, "socket send start typing from user $fromUser, chat ID: $chatId")
        socket?.emit(TYPING, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
        })
    }

    fun sendStopTyping(chatId: String, fromUser: String) {
        AppLogger.i(TAG, "socket send stop typing from user $fromUser, chat ID: $chatId")
        socket?.emit(STOP_TYPING, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
        })
    }


    fun disconnect() {
        AppLogger.i(TAG, "socket closed")
        socket?.close()
        socket = null
    }

    companion object {
        private const val CHAT_ID = "chatId"
        private const val FROM_USER = "fromUser"
        private const val MESSAGE = "message"
        private const val USER_STATUS_CHANGE = "userStatusChange"
        private const val CHAT_ROOM_USER_LIST = "chatRoomUserList"
        private const val INITIATOR_ID = "initiatorId"
        private const val JOIN_ROOM = "joinRoom"
        private const val PRIVATE_MESSAGE = "privateMessage"

        private const val PATH = "/socket.io/"
        private const val SOCKET_URL = "wss://mbank.gurkhahr.com"
        private const val RECONNECTION_DELAY = 5000L
        private const val SOCKET_PREFIX = 5000L
        private const val RECONNECTION_ATTEMPTS = 3
        private const val PRIVATE = "private"
        private const val TYPING = "typing"
        private const val STOP_TYPING = "stopTyping"
        private val TRANSPORTS = listOf("websocket")

        private const val TAG = "SocketManager"

    }
}