package com.gurkha.hr.domain.note.allNotes.mapper

import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.model.note.NotesResponseDto

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