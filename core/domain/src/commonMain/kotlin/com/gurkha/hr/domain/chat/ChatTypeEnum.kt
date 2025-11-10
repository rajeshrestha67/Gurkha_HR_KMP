package com.gurkha.hr.domain.chat

enum class ChatTypeEnum(val value: String) {
    EMPLOYEE("EMPLOYEE"),
    CUSTOMER("CUSTOMER");

    companion object {
        val list: List<ChatTypeEnum>
            get() = entries.toList().map { it }
    }
}