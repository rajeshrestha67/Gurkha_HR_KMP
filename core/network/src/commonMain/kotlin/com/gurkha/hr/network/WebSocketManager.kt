package com.gurkha.hr.network

import com.gurkha.model.chat.Content
import com.gurkha.model.chat.SendChatMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.utils.io.InternalAPI
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
    private var reconnectAttempts = 0

    private val _onContent = MutableSharedFlow<Content>()
    val onContent: SharedFlow<Content> = _onContent

    private val _onTyping = MutableSharedFlow<String?>()
    val onTyping: SharedFlow<String?> = _onTyping

    private val _onConnect = MutableSharedFlow<Unit>()
    val onConnect: SharedFlow<Unit> = _onConnect

    @OptIn(InternalAPI::class)
    suspend fun connect(username: String) {
        session = client.webSocketSession(
            method = HttpMethod.Get,
            host = "mbank.gurkhahr.com",
            port = 443,
            path = "/socket.io/",
        ) {
            url {
                protocol = URLProtocol.WSS
                parameters.append("username", username)
                parameters.append("socketPrefix", "mbank")
                parameters.append("EIO", "3")
                parameters.append("transport", "websocket")
            }
        }

        session?.start()
        println("✅ WebSocket connected: ${session != null}")
        // listenMessages()
    }

    suspend fun listenMessages() {
        session?.incoming?.receiveAsFlow()?.collect {
            println("data $it")
        }
        session?.incoming?.consumeEach { frame ->
            println("frame $frame, ${frame.readBytes()}")
            when (frame) {
                is Frame.Text -> {
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
                                val fromUser =
                                    json["data"]?.jsonObject?.get("fromUser")?.jsonPrimitive?.content
                                _onTyping.emit(fromUser)
                            }

                            "stopTyping" -> {
                                _onTyping.emit(null)
                            }
                        }
                    } catch (e: Exception) {
                        println("Error parsing message: ${e.message}")
                    }
                }

                is Frame.Binary -> {
                    println("📩 Received binary data: ${frame.data.size} bytes")
                }

                else -> {}
            }
        }
    }

    suspend fun sendMessage(sendChatMessage: SendChatMessage) {
//        val byte = Json.encodeToString(sendChatMessage)
//        session?.send(Frame.Text(message)) ?: println("⚠️ No active session")
        // session?.send(frame = Frame.Text(byte)) ?: println("⚠️ No active session")
        emit("privateMessage", buildJsonObject {
            put("chatId", sendChatMessage.chatId)
            put("fromUser", sendChatMessage.fromUser)
            put("message", sendChatMessage.message)
        })
    }

    @OptIn(InternalAPI::class)
    suspend fun emit(event: String, payload: JsonObject) {
        val json = buildJsonObject {
            put("event", event)
            put("data", payload)
        }
        val obj = Json.encodeToString(json)
        println("obj $obj")
        session?.send(obj)

    }

    suspend fun emitJoinRoom(chatId: String, fromUser: String) {
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