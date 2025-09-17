package com.gurkha.di

import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.settings.model.ChangePasswordViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class ChangePasswordModule {

    @KoinViewModel
    fun getChangePasswordViewModel(
        passwordValidateUseCase: PasswordValidateUseCase
    ) = ChangePasswordViewModel(newPasswordValidateUseCase = passwordValidateUseCase)
}