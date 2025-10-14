package com.gurkha.di

import com.gurkha.hr.addNoteScreen.AddNotesViewModel
import com.gurkha.hr.data.note.KtorNoteRemoteRepository
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.note.addNote.useCase.AddNoteUseCase
import com.gurkha.hr.domain.note.allNotes.repository.NoteRemoteRepository
import com.gurkha.hr.domain.note.allNotes.useCase.NoteUseCase
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

    @KoinViewModel
    fun getAddNotesViewModel(
        requiredValidationUseCase: RequiredValidationUseCase,
        addNoteUseCase: AddNoteUseCase
    ): AddNotesViewModel = AddNotesViewModel(
        requiredValidationUseCase = requiredValidationUseCase,
        addNoteUseCase = addNoteUseCase
    )

    @KoinViewModel
    fun getNoteViewModel(
        noteUseCase: NoteUseCase
    ): NoteViewModel = NoteViewModel(
        noteUseCase = noteUseCase
    )
}