package com.gurkha.hr.domain.note.useCase

import com.gurkha.hr.domain.note.mapper.toData
import com.gurkha.hr.domain.note.model.NoteData
import com.gurkha.hr.domain.note.repository.NoteRemoteRepository
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