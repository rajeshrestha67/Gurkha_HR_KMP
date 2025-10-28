package com.gurkha.hr.model.note

import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.model.note.ui.AddedNoteDataUi

data class NoteState(
    val noteItem: List<NoteData> = emptyList(),
    val isFetchingNotes: Boolean = false,
    val hasUpdatedData: Boolean = false,
    val isDeletingData : Boolean = false,
    val selectedId : Int? = null,
    val isUpdate : Boolean = false,
    val noteItemToEdit : AddedNoteDataUi? = null,
    val isRefreshing : Boolean = false
)
