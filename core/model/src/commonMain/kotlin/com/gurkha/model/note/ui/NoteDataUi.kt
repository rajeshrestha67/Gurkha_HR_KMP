package com.gurkha.model.note.ui

import kotlinx.serialization.Serializable

@Serializable
data class NoteDataUi(
    val id: Int ,
    val title : String,
    val description : String,
    val isEvent: String,
    val startDateAD: String,
    val endDateAD: String,
    val startDateBS: String,
    val endDateBS: String,
    val location: String,
    val active: String,
    val startTime: String,
    val endTime: String,
    val isReminder: String
)

