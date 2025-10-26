package com.gurkha.model.chat.list

import kotlinx.serialization.Serializable


@Serializable
data class ChatMessageResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: ChatMessageDetailResponseDto? = null
)

@Serializable
data class ChatMessageDetailResponseDto(
    val content: List<ChatMessageContentResponseDto>? = null,
    val pageable: ChatMessagePageableResponseDto? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null,
    val last: Boolean? = null,
    val size: Int? = null,
    val number: Int? = null,
    val sort: ChatMessagePagingSortResponseDto? = null,
    val numberOfElements: Int? = null,
    val first: Boolean? = null,
    val empty: Boolean? = null
)

@Serializable
data class ChatMessageContentResponseDto(
    val type: String? = null,
    val fromUser: String? = null,
    val content: String? = null,
    val chatId: String? = null,
    val id: String? = null,
    val createdDate: String? = null
)

@Serializable
data class ChatMessagePageableResponseDto(
    val pageNumber: Int? = null,
    val pageSize: Int? = null,
    val sort: ChatMessagePagingSortResponseDto? = null,
    val offset: Int? = null,
    val paged: Boolean? = null,
    val unpaged: Boolean? = null
)

@Serializable
data class ChatMessagePagingSortResponseDto(
    val sorted: Boolean? = null,
    val empty: Boolean? = null,
    val unsorted: Boolean? = null
)