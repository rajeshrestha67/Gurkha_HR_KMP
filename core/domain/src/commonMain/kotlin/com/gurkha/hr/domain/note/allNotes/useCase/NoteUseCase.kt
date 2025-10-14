package com.gurkha.hr.domain.note.allNotes.useCase

import com.gurkha.hr.domain.note.allNotes.mapper.toData
import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.hr.domain.note.allNotes.repository.NoteRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class NoteUseCase(
    private val noteRemoteRepository: NoteRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<NoteData>, DataError> {
        return noteRemoteRepository.fetchAllNotes().map {
            it.toData()
        }
    }
}