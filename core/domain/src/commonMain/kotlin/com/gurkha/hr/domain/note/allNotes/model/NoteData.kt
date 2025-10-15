package com.gurkha.hr.domain.note.allNotes.model

import com.gurkha.model.note.ui.NoteDataUi


data class NoteData(
    val id: Int ,
    val title: String ,
    val description: String ,
    val isEvent: String ,
    val startDateAD: String ,
    val endDateAD: String ,
    val startDateBS: String ,
    val endDateBS: String ,
    val location: String ,
    val active: String ,
    val startTime: String ,
    val endTime: String ,
    val isReminder: String ,
)

fun NoteData.toUi(): NoteDataUi {
    return NoteDataUi(
        id = id,
        title = title,
        description = description,
        isEvent = isEvent,
        isReminder = isReminder,
        active = active,
        startDateAD = startDateAD,
        endDateAD = endDateAD,
        startDateBS = startDateBS,
        endDateBS = endDateBS,
        location = location,
        startTime = startTime,
        endTime = endTime,
    )
}


