package com.gurkha.model.note.addNote
import kotlinx.serialization.Serializable

@Serializable
data class AddNoteResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: NoteDetailDto? = null,
    val success: Boolean? = null
)

@Serializable
data class NoteDetailDto(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val isEvent: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val active: String? = null,
    val isReminder: String? = null,
    val reminderTime: String? = null,
    val reminderDate: String? = null,
    val reminderMessage: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val location: String? = null
)

