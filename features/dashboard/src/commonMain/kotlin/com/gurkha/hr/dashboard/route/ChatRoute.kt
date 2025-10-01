package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface ChatRoute {
    @Serializable
    data object ChatList : ChatRoute

    @Serializable
    data class ChatRoom(val json: String) : ChatRoute
}