package com.gurkha.model.note.ui

import kotlinx.serialization.Serializable

@Serializable
data class AddedNoteDataUi(
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
