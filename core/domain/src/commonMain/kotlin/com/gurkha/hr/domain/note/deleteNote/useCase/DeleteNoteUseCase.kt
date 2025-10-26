package com.gurkha.hr.domain.note.deleteNote.useCase

import com.gurkha.hr.domain.note.allNotes.repository.NoteRemoteRepository
import com.gurkha.hr.domain.note.deleteNote.mapper.toData
import com.gurkha.hr.domain.note.deleteNote.model.DeleteNoteData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class DeleteNoteUseCase(
    private val noteRemoteRepository: NoteRemoteRepository
) {
    suspend operator fun invoke(
        id:Int
    ): ERPResult<DeleteNoteData, DataError>{
        return noteRemoteRepository.deleteNote(id = id).map {
            it.toData()
        }
    }
}