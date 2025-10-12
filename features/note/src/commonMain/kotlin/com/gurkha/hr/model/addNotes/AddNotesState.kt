package com.gurkha.hr.model.addNotes

import com.gurkha.hr.components.date.DateData
import org.jetbrains.compose.resources.StringResource

data class AddNotesState(
    val isEvent: Boolean = false,
    val title: String = "",
    val description: String = "",

    val titleError: StringResource? = null,
    val descriptionError: StringResource? = null,

    val selectedHeaderTab: Int = 1,

    val startDate: DateData? = null,
    val endDate: DateData? = null,
    val startTime: String = "",
    val endTime: String = "",
    val location: String = "",

    val startDateError: StringResource? = null,
    val endDateError: StringResource? = null,
    val startTimeError: StringResource? = null,
    val endTimeError: StringResource? = null,
    val locationError: StringResource? = null
)