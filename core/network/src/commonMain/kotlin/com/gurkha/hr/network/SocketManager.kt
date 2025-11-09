package com.gurkha.hr.network

import com.gurkha.hr.logger.AppLogger
import com.gurkha.model.chat.Content
import com.piasy.kmp.socketio.socketio.IO
import com.piasy.kmp.socketio.socketio.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SocketManager {
    var socket: Socket? = null

    private val _onContent = MutableStateFlow<Content?>(null)
    val onContent: StateFlow<Content?> = _onContent

    private val _onTyping = MutableStateFlow(false)
    val onTyping: StateFlow<Boolean> = _onTyping

    //    private val _onTypingStop = MutableStateFlow(Unit)
//    val onTypingStop: StateFlow<Unit> = _onTypingStop
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    //private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    fun connect(username: String, chatId: String, socketPrefix: String) {
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
                put(USERNAME, username)
                put(SOCKET_PREFIX, socketPrefix)
            }
        }
        IO.socket(SOCKET_URL, opts) { socket ->
            socket.on(Socket.EVENT_CONNECT) {
                AppLogger.i(TAG, "socket connected")
                this.socket = socket
                _isConnected.update {
                    true
                }
//                CoroutineScope(Dispatchers.IO).launch {
//                    _onConnect.emit(Unit)
//                }
            }
            socket.on(Socket.EVENT_DISCONNECT) {
                AppLogger.i(TAG, "socket disconnected")
                this.socket = null
                _isConnected.update {
                    false
                }
            }
            socket.on(Socket.EVENT_CONNECT_ERROR) {
                AppLogger.i(TAG, "socket connection error")
                this.socket = null
                _isConnected.update {
                    it
                    false
                }
            }
            socket.on("$PRIVATE:$chatId") { args ->
                args.firstOrNull()?.let { arg ->
                    if (arg is JsonObject) {
                        val json = Json { ignoreUnknownKeys = true }
                        val content = json.decodeFromString<Content>(string = arg.toString())
                        AppLogger.i(TAG, "socket new message $arg")
                        _onContent.update {
                            content
                        }
                    }
                }
            }
            socket.on("$TYPING:$chatId") { args ->
                args.firstOrNull()?.let { arg ->
                    AppLogger.i(TAG, "socket typing")
                    _onTyping.update {
                        true
                    }
                }
            }

            socket.on("$STOP_TYPING:$chatId") { args ->
                args.firstOrNull()?.let { arg ->
                    AppLogger.i(TAG, "socket stop typing")
                    _onTyping.update {
                        false
                    }
                }
            }
            socket.open()
        }
    }


    fun joinRoom(chatId: String, fromUser: String, initiatorId: String) {
        AppLogger.i(TAG, "socket room join")
        socket?.emit(JOIN_ROOM, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
            put(INITIATOR_ID, initiatorId)
        })
    }

    @OptIn(ExperimentalUuidApi::class)
    fun sendMessage(chatId: String, fromUser: String, message: String) {
        AppLogger.i(TAG, "socket send message")
        val id = Uuid.random().toString()
        socket?.emit(PRIVATE_MESSAGE, buildJsonObject {
            put("id", id)
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
            put(MESSAGE, message)
        })
    }


    fun sendStartTyping(chatId: String, fromUser: String) {
        AppLogger.i(TAG, "socket send start typing")
        socket?.emit(TYPING, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
        })
    }

    fun sendStopTyping(chatId: String, fromUser: String) {
        AppLogger.i(TAG, "socket send stop typing")
        socket?.emit(STOP_TYPING, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
        })
    }


    fun disconnect() {
        AppLogger.i(TAG, "socket closed")
        socket?.close()
//        scope.cancel()
        socket = null
    }

    companion object {
        private const val CHAT_ID = "chatId"
        private const val FROM_USER = "fromUser"
        private const val USERNAME = "username"
        private const val MESSAGE = "message"
        private const val INITIATOR_ID = "initiatorId"
        private const val SOCKET_PREFIX = "socketPrefix"
        private const val JOIN_ROOM = "joinRoom"
        private const val PRIVATE_MESSAGE = "privateMessage"

        private const val PATH = "/socket.io/"
        private const val SOCKET_URL = "wss://mbank.gurkhahr.com"
        private const val RECONNECTION_DELAY = 5000L
        private const val RECONNECTION_ATTEMPTS = 3
        private const val PRIVATE = "private"
        private const val TYPING = "typing"
        private const val STOP_TYPING = "stopTyping"
        private val TRANSPORTS = listOf("websocket")

        private const val TAG = "SocketManager"

    }
}