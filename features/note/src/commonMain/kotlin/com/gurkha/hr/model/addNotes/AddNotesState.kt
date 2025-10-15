package com.gurkha.hr.model.addNotes

import com.gurkha.hr.components.date.DateData
import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.model.note.ui.AddedNoteDataUi
import com.gurkha.model.note.ui.NoteDataUi
import org.jetbrains.compose.resources.StringResource

data class AddNotesState(
    val isEvent: Boolean = false,
    val title: String = "",
    val description: String = "",
    val isReminder : String = "N",

    val titleError: StringResource? = null,
    val descriptionError: StringResource? = null,

    val selectedHeaderTab: Int = 1,

    val startDate: DateData? = null,
    val endDate: DateData? = null,
    val startTime: String = "",
    val endTime: String = "",
    val location: String = "",

    val reminderTime : String = "",
    val reminderDate : String = "",
    val reminderMessage : String = "",

    val startDateError: StringResource? = null,
    val endDateError: StringResource? = null,
    val startTimeError: StringResource? = null,
    val endTimeError: StringResource? = null,
    val locationError: StringResource? = null,

    val noteItemData : NoteDataUi? =null,
    val storeNoteItem : AddedNoteDataUi? =null,

    val isEdit : Boolean = false,

    val isUpdating : Boolean = false,
    val isAdding : Boolean = false
)