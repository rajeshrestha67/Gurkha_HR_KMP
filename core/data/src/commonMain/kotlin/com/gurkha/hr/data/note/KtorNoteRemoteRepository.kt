package com.gurkha.hr.data.note

import com.gurkha.hr.domain.note.allNotes.repository.NoteRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.note.NotesRequestDto
import com.gurkha.model.note.NotesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorNoteRemoteRepository(
    private val httpClient: HttpClient
) : NoteRemoteRepository {
    override suspend fun fetchAllNotes(): ERPResult<NotesResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.ALL_NOTES_END_POINT
            )
        }
    }

    override suspend fun addNote(
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
    ): ERPResult<NotesResponseDto, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.CREATE_NOTE_END_POINT
            ) {
                setBody(
                    NotesRequestDto(
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
                        )
                )
            }
        }
    }
}