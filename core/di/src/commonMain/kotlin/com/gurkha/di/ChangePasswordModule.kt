package com.gurkha.di

import com.gurkha.hr.data.change_password.KtorChangePasswordRemoteRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.changePassword.repository.ChangePasswordRemoteRepository
import com.gurkha.hr.domain.changePassword.usecase.ChangePasswordUseCase
import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.settings.ChangePasswordViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class ChangePasswordModule {

    @Factory(binds = [ChangePasswordRemoteRepository::class])
    fun changePasswordRepository(httpClient: HttpClient) =
        KtorChangePasswordRemoteRepository(httpClient)

    @Factory
    fun changePasswordUseCase(
        changePasswordRemoteRepository: ChangePasswordRemoteRepository,
        userDataRepository: UserDataRepository
    ) = ChangePasswordUseCase(
        changePasswordRemoteRepository = changePasswordRemoteRepository,
        userDataRepository = userDataRepository
    )


    @KoinViewModel
    fun getChangePasswordViewModel(
        passwordValidateUseCase: PasswordValidateUseCase,
        changePasswordUseCase: ChangePasswordUseCase
    ) = ChangePasswordViewModel(
        newPasswordValidateUseCase = passwordValidateUseCase,
        changePasswordUseCase = changePasswordUseCase
    )
}