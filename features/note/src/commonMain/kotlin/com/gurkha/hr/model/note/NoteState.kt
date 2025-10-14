package com.gurkha.hr.model.note

import com.gurkha.hr.domain.note.allNotes.model.NoteData

data class NoteState(
    val noteItem: List<NoteData> = emptyList(),
    val isFetchingNotes: Boolean = false,
    val hasUpdatedData: Boolean = false,
    val selectedId : Int? = null
)
