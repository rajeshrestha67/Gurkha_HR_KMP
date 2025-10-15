package com.gurkha.hr.model.addNotes

import com.gurkha.hr.components.date.DateData
import com.gurkha.model.note.ui.AddedNoteDataUi

sealed interface AddNotesAction {
    data class OnTitleChange(val title: String) : AddNotesAction
    data class OnDescriptionChange(val description: String) : AddNotesAction

    data class OnHeaderTabChange(val tab: Int) : AddNotesAction

    data class OnStartDateChange(val date: DateData) : AddNotesAction
    data class OnEndDateChange(val date: DateData) : AddNotesAction
    data class OnStartTimeChange(val time: String) : AddNotesAction
    data class OnEndTimeChange(val time: String) : AddNotesAction

    data class OnLocationChange(val location: String) : AddNotesAction
    data object OnToggleEvent : AddNotesAction
    data object OnSubmit : AddNotesAction

    data object OnIsEditChange : AddNotesAction
    data class OnUpdateNoteData(val item: String) : AddNotesAction

    data object UpdateNote : AddNotesAction

    data class OnUpdateDataForStore(val data: String) : AddNotesAction

}