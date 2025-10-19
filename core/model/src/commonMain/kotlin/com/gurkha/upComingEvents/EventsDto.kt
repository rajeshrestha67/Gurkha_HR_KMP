package com.gurkha.upComingEvents

import kotlinx.serialization.Serializable

@Serializable
data class EventsResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<EventDetailDto>? = null,
    val success: Boolean? = null
)

@Serializable
data class EventDetailDto(
    val name: String? = null,
    val description: String? = null,
    val fromDateBs: String? = null,
    val toDateBs: String? = null,
    val isNotice: String? = null
)