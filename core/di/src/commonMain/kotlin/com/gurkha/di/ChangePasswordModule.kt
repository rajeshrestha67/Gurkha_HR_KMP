package com.gurkha.di

import com.gurkha.hr.data.change_password.KtorChangePasswordRemoteRepository
import com.gurkha.hr.data.login.KtorUserRemoteRepository
import com.gurkha.hr.domain.changePassword.repository.ChangePasswordRemoteRepository
import com.gurkha.hr.domain.changePassword.usecase.ChangePasswordUseCase
import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.settings.model.ChangePasswordViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class ChangePasswordModule {

    @Factory(binds = [ChangePasswordRemoteRepository::class])
    fun changePasswordRepository(httpClient: HttpClient) = KtorChangePasswordRemoteRepository(httpClient)

    @Factory
    fun changePasswordUseCase(changePasswordRemoteRepository: ChangePasswordRemoteRepository) =
        ChangePasswordUseCase(changePasswordRemoteRepository)




    @KoinViewModel
    fun getChangePasswordViewModel(
        passwordValidateUseCase: PasswordValidateUseCase,
        changePasswordUseCase: ChangePasswordUseCase
    ) = ChangePasswordViewModel(
        newPasswordValidateUseCase = passwordValidateUseCase,
        changePasswordUseCase = changePasswordUseCase
    )
}