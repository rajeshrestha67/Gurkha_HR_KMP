package com.gurkha.model.note

import kotlinx.serialization.Serializable

@Serializable
data class NotesRequestDto(
    val title: String? = null,
    val description: String? = null,
    val isEvent: String? = null,
    val startDateAD: String? = null,
    val endDateAD: String? = null,
    val location: String? = null,
    val active: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val isReminder: String? = null,
    val reminderMessage: String? = null,
    val reminderTime: String? = null
)
@Serializable
data class NotesUpdateRequestDto(
    val id: Int,
    val title: String,
    val description: String,
    val isEvent: String,
    val isReminder: String,
    val active: String,
    val location: String,
    val startTime: String,
    val endTime: String,
    val reminderMessage: String,
    val reminderTime: String
)
@Serializable
data class NotesResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<NoteDetailDto>? = null,
    val success: Boolean? = null
)

@Serializable
data class NoteDetailDto(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val isEvent: String? = null,
    val startDateAD: String? = null,
    val endDateAD: String? = null,
    val startDateBS: String? = null,
    val endDateBS: String? = null,
    val location: String? = null,
    val active: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val isReminder: String? = null,
    val createdAtAd: String? = null,
    val createdAtBs: String? = null
)

