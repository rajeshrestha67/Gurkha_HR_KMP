package com.gurkha.di

import com.gurkha.hr.domain.form.EmailValidateUseCase
import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class FormModule {

    @Single
    fun emailValidateUseCase() = EmailValidateUseCase()

    @Single
    fun passwordValidateUseCase() = PasswordValidateUseCase()

    @Single
    fun requiredValidation() = RequiredValidationUseCase()
}