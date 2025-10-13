package com.gurkha.hr.model.note

import com.gurkha.hr.domain.note.model.NoteData

data class NoteState(
    val noteItem : List<NoteData> = emptyList(),
    val isFetchingNotes : Boolean = false
)
