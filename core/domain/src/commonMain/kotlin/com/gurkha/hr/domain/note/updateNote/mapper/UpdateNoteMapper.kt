package com.gurkha.hr.domain.note.updateNote.mapper

import com.gurkha.hr.domain.note.updateNote.model.UpdateNoteData
import com.gurkha.model.note.NotesResponseDto

fun NotesResponseDto.toData(): UpdateNoteData{
    return UpdateNoteData(
        status = status ?: "",
        success = success ?: false,
        message = message ?: ""
    )
}