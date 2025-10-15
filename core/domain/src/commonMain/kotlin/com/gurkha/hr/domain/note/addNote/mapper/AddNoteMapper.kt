package com.gurkha.hr.domain.note.addNote.mapper

import com.gurkha.hr.domain.note.addNote.model.AddNoteData
import com.gurkha.model.note.addNote.AddNoteResponseDto

fun AddNoteResponseDto.toData(): AddNoteData {
    val detail = this.detail
    return AddNoteData(
        id = detail?.id ?: 0,
        title = detail?.title ?: " ",
        description = detail?.description ?: "",
        isEvent = detail?.isEvent.toString(),
        startTime = detail?.startTime ?: "",
        endTime = detail?.endTime ?: "",
        startDate = detail?.startDate ?: "",
        endDate = detail?.endDate ?: "",
        active = detail?.active.toString(),
        isReminder = detail?.isReminder.toString(),
        reminderTime = detail?.reminderTime ?: "",
        reminderDate = detail?.reminderDate ?: "",
        reminderMessage = detail?.reminderMessage ?: "",
        createdAt = detail?.createdAt ?: "",
        updatedAt = detail?.updatedAt ?: "",
        location = detail?.location ?: "",
        message = this.message ?: ""
    )
}




