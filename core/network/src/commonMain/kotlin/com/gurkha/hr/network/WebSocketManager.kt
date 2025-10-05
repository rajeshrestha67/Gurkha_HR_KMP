package com.gurkha.hr.network

import com.gurkha.model.chat.Content
import com.gurkha.model.chat.SendChatMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.encodeURLPath
import io.ktor.utils.io.InternalAPI
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

class WebSocketManager(
    private val client: HttpClient,
) {
    private var session: DefaultClientWebSocketSession? = null

    private val _onContent = MutableSharedFlow<Content>()
    val onContent: SharedFlow<Content> = _onContent

    private val _onTyping = MutableSharedFlow<String?>()
    val onTyping: SharedFlow<String?> = _onTyping

    private val _onConnect = MutableSharedFlow<Unit>()
    val onConnect: SharedFlow<Unit> = _onConnect

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    @OptIn(InternalAPI::class)
    suspend fun connect(username: String) {
        if (_isConnected.value) {
            println("⚠️ Already connected")
            return
        }

        try {
            val query = listOf(
                "username=${username.encodeURLPath()}",
                "socketPrefix=mbank",
                "EIO=3",
                "transport=websocket"
            ).joinToString("&")

            session = client.webSocketSession(
                urlString = "wss://mbank.gurkhahr.com/socket.io/?$query"
            )
            _isConnected.value = session?.isActive == true
            if (_isConnected.value) {
                println("✅ WebSocket connected")
                _onConnect.emit(Unit)
            } else {
                println("❌ Failed to connect WebSocket")
            }
            listenMessages()

        } catch (e: Exception) {
            println("🚨 WebSocket connection error: ${e.message}")
            _isConnected.value = false
        }
    }

    suspend fun listenMessages() {
        try {
            session?.incoming?.consumeEach { frame ->
                when (frame) {
                    is Frame.Text -> handleTextFrame(frame)
                    is Frame.Binary -> println("📩 Received binary data: ${frame.data.size} bytes")
                    is Frame.Close -> {
                        println("❌ WebSocket closed")
                        _isConnected.value = false
                    }

                    else -> {}
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("🚨 Error while listening: ${e.message}")
            _isConnected.value = false
        }
    }

    private suspend fun handleTextFrame(frame: Frame.Text) {
        val text = frame.readText()
        println("Received: $text")
        try {
            val json = Json.decodeFromString<JsonObject>(text)
            when (json["event"]?.jsonPrimitive?.content) {
                "privateMessage" -> {
                    val data = Json.decodeFromJsonElement<Content>(json["data"]!!)
                    _onContent.emit(data)
                }

                "typing" -> {
                    val fromUser = json["data"]?.jsonObject?.get("fromUser")?.jsonPrimitive?.content
                    _onTyping.emit(fromUser)
                }

                "stopTyping" -> _onTyping.emit(null)
            }
        } catch (e: Exception) {
            println("Error parsing message: ${e.message}")
        }
    }

    suspend fun sendMessage(sendChatMessage: SendChatMessage) {
        if (!_isConnected.value || session == null) {
            println("⚠️ Not connected — message not sent")
            return
        }
        emit("privateMessage", buildJsonObject {
            put("chatId", sendChatMessage.chatId)
            put("fromUser", sendChatMessage.fromUser)
            put("message", sendChatMessage.message)
        })
    }

    @OptIn(InternalAPI::class)
    suspend fun emit(event: String, payload: JsonObject) {
        if (!_isConnected.value || session == null) {
            println("⚠️ Cannot emit, socket not connected")
            return
        }
        println("called join room emit")
        val json = buildJsonObject {
            put("event", event)
            put("data", payload)
        }
        val obj = Json.encodeToString(json)
        println("📤 Sending: $obj")
        println("called join room emit sending")

        session?.send(obj)

    }

    suspend fun emitJoinRoom(chatId: String, fromUser: String) {
        println("called join room")
        emit("joinRoom", buildJsonObject {
            put("chatId", chatId)
            put("fromUser", fromUser)
            put("initiatorId", "app_mbank")
        })
    }

    suspend fun disconnect() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client closed"))
        session = null
        println("❌ WebSocket disconnected")
    }
}