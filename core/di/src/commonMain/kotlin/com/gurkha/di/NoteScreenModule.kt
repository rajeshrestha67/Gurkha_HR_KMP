package com.gurkha.di

import com.gurkha.hr.addNoteScreen.AddNotesViewModel
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class NoteScreenModule {

    @Factory
    fun getAddNotesViewModel(
        requiredValidationUseCase: RequiredValidationUseCase,
    ): AddNotesViewModel = AddNotesViewModel(
        requiredValidationUseCase= requiredValidationUseCase,
    )
}