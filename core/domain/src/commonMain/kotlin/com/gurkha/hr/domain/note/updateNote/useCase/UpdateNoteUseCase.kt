package com.gurkha.hr.domain.note.updateNote.useCase

import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.hr.domain.note.allNotes.repository.NoteRemoteRepository
import com.gurkha.hr.domain.note.updateNote.mapper.toData
import com.gurkha.hr.domain.note.updateNote.model.UpdateNoteData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class UpdateNoteUseCase(
    private val noteRemoteRepository: NoteRemoteRepository
) {
    suspend operator fun invoke(
        id: Int,
        title: String,
        description: String,
        isEvent: String,
        isReminder: String,
        active: String,
        location: String,
        startTime: String,
        endTime: String,
        reminderMessage: String,
        reminderTime: String
    ): ERPResult<UpdateNoteData, DataError> {
        return noteRemoteRepository.updateNote(
            active = active,
            description = description,
            endTime = endTime,
            isEvent = isEvent,
            isReminder = isReminder,
            location = location,
            reminderMessage = reminderMessage,
            reminderTime = reminderTime,
            startTime = startTime,
            title = title,
            id = id
        ).map {
            it.toData()
        }
    }
}