package com.gurkha.hr.domain.note.addNote.model

data class AddNoteData(
    val id: Int,
    val title: String,
    val description: String,
    val isEvent: String,
    val startTime: String ,
    val endTime: String ,
    val startDate: String ,
    val endDate: String ,
    val active: String,
    val isReminder: String,
    val reminderTime: String ,
    val reminderDate: String ,
    val reminderMessage: String ,
    val createdAt: String,
    val updatedAt: String,
    val location: String ,
    val message: String
)

