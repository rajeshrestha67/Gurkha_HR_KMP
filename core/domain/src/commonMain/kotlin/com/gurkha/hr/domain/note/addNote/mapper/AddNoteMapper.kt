package com.gurkha.hr.domain.note.addNote.mapper

import com.gurkha.hr.domain.note.addNote.model.AddNoteData
import com.gurkha.model.note.NotesResponseDto

fun NotesResponseDto.toData(): AddNoteData{
    return AddNoteData(
        status = status ?: "",
        message = message ?: "",
        success = success ?: false
    )
}