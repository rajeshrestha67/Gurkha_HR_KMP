package com.gurkha.model.chat

import kotlinx.serialization.Serializable


@Serializable
enum class ChatTypeEnum(val value: String) {
    EMPLOYEE("EMPLOYEE"),
    SUPPORT("SUPPORT");

    companion object {
        val list: List<ChatTypeEnum>
            get() = entries.toList().map { it }
    }
}