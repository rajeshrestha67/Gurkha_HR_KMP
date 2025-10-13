package com.gurkha.hr.data.note

import com.gurkha.hr.domain.note.repository.NoteRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.note.NotesResponseDto
import io.ktor.client.HttpClient

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
}