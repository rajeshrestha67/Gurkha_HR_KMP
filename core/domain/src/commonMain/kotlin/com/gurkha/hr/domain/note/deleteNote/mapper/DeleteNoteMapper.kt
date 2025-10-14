package com.gurkha.hr.domain.note.deleteNote.mapper

import com.gurkha.hr.domain.note.deleteNote.model.DeleteNoteData
import com.gurkha.model.note.NotesResponseDto

fun NotesResponseDto.toData(): DeleteNoteData{
    return DeleteNoteData(
        success = success ?: false,
        message = message ?: "",
        status = status ?: ""
    )
}