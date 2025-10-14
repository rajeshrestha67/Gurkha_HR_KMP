package com.gurkha.hr.domain.note.allNotes.mapper

import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.model.note.NotesResponseDto
import com.gurkha.model.note.ui.NoteDataUi

fun NotesResponseDto.toData(): List<NoteData> {
    return detail?.map {
        NoteData(
            id = it.id ?: 0,
            title = it.title ?: "",
            description = it.description ?: "",
            isEvent = it.isEvent ?: "N",
            startDateAD = it.startDateAD ?: "--:--",
            endDateAD = it.endDateAD ?: "--:--",
            startDateBS = it.startDateBS ?: "--:--",
            endDateBS = it.endDateBS ?: "--:--",
            location = it.location ?: "",
            active = it.active ?: "",
            startTime = it.startTime ?: "--:--",
            endTime = it.endTime ?: "--:--",
            isReminder = it.isReminder ?:"N"
        )
    }?: emptyList()
}

fun NoteData.toUi(): NoteDataUi{
    return NoteDataUi(
        id = this.id ,
        title = this.title,
        description = this.description ,
        isEvent = this.isEvent,
        startDateAD = this.startDateAD,
        endDateAD = this.endDateAD,
        startDateBS = this.startDateBS,
        endDateBS = this.endDateBS,
        location = this.location,
        active = this.active,
        startTime = this.startTime,
        endTime = this.endTime,
        isReminder = this.isReminder

    )
}