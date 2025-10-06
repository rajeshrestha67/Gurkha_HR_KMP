package com.gurkha.hr.network

import com.gurkha.model.chat.Content
import com.piasy.kmp.socketio.socketio.IO
import com.piasy.kmp.socketio.socketio.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SocketManager {
    var socket: Socket? = null
    private val _onConnect = MutableSharedFlow<Unit>(replay = 1)
    val onConnect: SharedFlow<Unit> = _onConnect

    private val _onContent = MutableSharedFlow<Content>()
    val onContent: SharedFlow<Content> = _onContent

    private val _onTyping = MutableSharedFlow<Unit>()
    val onTyping: SharedFlow<Unit> = _onTyping
    private val _onTypingStop = MutableSharedFlow<Unit>()
    val onTypingStop: SharedFlow<Unit> = _onTypingStop
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
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
                this.socket = socket
                _isConnected.update {
                    true
                }
                scope.launch {
                    _onConnect.emit(Unit)
                }
            }
            socket.on(Socket.EVENT_DISCONNECT) {
                this.socket = null
                _isConnected.update {
                    false
                }
            }
            socket.on(Socket.EVENT_CONNECT_ERROR) {
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
                        scope.launch {
                            _onContent.emit(content)
                        }
                    }
                }
            }
            socket.on("$TYPING:$chatId") {
                scope.launch {
                    _onTyping.emit(Unit)
                }

            }
            
            socket.on("$STOP_TYPING:$chatId") {
                scope.launch {
                    _onTypingStop.emit(Unit)
                }
            }
            socket.open()
        }
    }


    fun joinRoom(chatId: String, fromUser: String, initiatorId: String) {
        socket?.emit(JOIN_ROOM, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
            put(INITIATOR_ID, initiatorId)
        })
    }

    fun sendMessage(chatId: String, fromUser: String, message: String) {
        socket?.emit(PRIVATE_MESSAGE, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
            put(MESSAGE, message)
        })
    }


    fun sendStartTyping(chatId: String, fromUser: String) {
        socket?.emit(TYPING, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
        })
    }

    fun sendStopTyping(chatId: String, fromUser: String) {
        socket?.emit(STOP_TYPING, buildJsonObject {
            put(CHAT_ID, chatId)
            put(FROM_USER, fromUser)
        })
    }


    fun disconnect() {
        socket?.close()
        scope.cancel()
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


    }
}