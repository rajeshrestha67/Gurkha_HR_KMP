package com.gurkha.hr.domain.note.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.note.NotesResponseDto

 interface NoteRemoteRepository {
    suspend fun fetchAllNotes(): ERPResult<NotesResponseDto, DataError>
}