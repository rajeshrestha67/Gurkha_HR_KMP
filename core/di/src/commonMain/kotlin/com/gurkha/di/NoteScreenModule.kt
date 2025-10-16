package com.gurkha.di

import com.gurkha.hr.addNoteScreen.AddNotesViewModel
import com.gurkha.hr.data.note.KtorNoteRemoteRepository
import com.gurkha.hr.detailNoteScreen.DetailNoteScreenViewModel
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.note.addNote.useCase.AddNoteUseCase
import com.gurkha.hr.domain.note.allNotes.repository.NoteRemoteRepository
import com.gurkha.hr.domain.note.allNotes.useCase.NoteUseCase
import com.gurkha.hr.domain.note.deleteNote.useCase.DeleteNoteUseCase
import com.gurkha.hr.domain.note.updateNote.useCase.UpdateNoteUseCase
import com.gurkha.hr.note.NoteViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class NoteScreenModule {
    @Factory(binds = [NoteRemoteRepository::class])
    fun noteRemoteRepository(httpClient: HttpClient): NoteRemoteRepository =
        KtorNoteRemoteRepository(httpClient = httpClient)

    @Factory
    fun noteUseCase(
        noteRemoteRepository: NoteRemoteRepository
    ): NoteUseCase = NoteUseCase(
        noteRemoteRepository = noteRemoteRepository
    )

    @Factory
    fun addNoteUseCase(
        noteRemoteRepository: NoteRemoteRepository
    ): AddNoteUseCase = AddNoteUseCase(
        noteRemoteRepository = noteRemoteRepository
    )

    @Factory
    fun updateNoteUseCase(
        noteRemoteRepository: NoteRemoteRepository
    ): UpdateNoteUseCase = UpdateNoteUseCase(
        noteRemoteRepository = noteRemoteRepository
    )

    @Factory
    fun deleteNoteUseCase(
        noteRemoteRepository: NoteRemoteRepository
    ): DeleteNoteUseCase = DeleteNoteUseCase(
        noteRemoteRepository = noteRemoteRepository
    )

    @KoinViewModel
    fun getAddNotesViewModel(
        requiredValidationUseCase: RequiredValidationUseCase,
        updateNoteUseCase: UpdateNoteUseCase,
        addNoteUseCase: AddNoteUseCase
    ): AddNotesViewModel = AddNotesViewModel(
        requiredValidationUseCase = requiredValidationUseCase,
        addNoteUseCase = addNoteUseCase,
        updateNoteUseCase = updateNoteUseCase
    )

    @KoinViewModel
    fun getNoteViewModel(
        deleteNoteUseCase: DeleteNoteUseCase,
        noteUseCase: NoteUseCase
    ): NoteViewModel = NoteViewModel(
        deleteNoteUseCase = deleteNoteUseCase,
        noteUseCase = noteUseCase
    )

    @KoinViewModel
    fun getDetailNoteScreenViewModel(
        deleteNoteUseCase: DeleteNoteUseCase,
    ): DetailNoteScreenViewModel = DetailNoteScreenViewModel(
        deleteNoteUseCase = deleteNoteUseCase
    )
}