package com.gurkha.hr.domain.note.allNotes.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.note.NotesResponseDto
import com.gurkha.model.note.addNote.AddNoteResponseDto

interface NoteRemoteRepository {
    suspend fun fetchAllNotes(): ERPResult<NotesResponseDto, DataError>

    suspend fun addNote(
        active: String,
        description: String,
        endTime: String,
        isEvent: String,
        isReminder: String,
        location: String,
        reminderMessage: String,
        reminderTime: String,
        startTime: String,
        title: String,
        startDate: String,
        endDate: String
    ): ERPResult<AddNoteResponseDto, DataError>

    suspend fun updateNote(
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
    ): ERPResult<NotesResponseDto, DataError>

    suspend fun deleteNote(
        id: Int
    ): ERPResult<NotesResponseDto, DataError>
}