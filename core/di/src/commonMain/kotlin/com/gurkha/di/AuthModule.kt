package com.gurkha.di

import com.gurkha.hr.data.login.KtorUserRemoteRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.domain.auth.login.usecase.ClearTokenUseCase
import com.gurkha.hr.domain.auth.login.usecase.LoginUseCase
import com.gurkha.hr.domain.form.EmailValidateUseCase
import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.domain.splash.UpdateFirstTimeCheckUseCase
import com.gurkha.hr.login.LoginViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class AuthModule {


    @Factory(binds = [UserRemoteRepository::class])
    fun userRepository(httpClient: HttpClient) = KtorUserRemoteRepository(httpClient)

    @Factory
    fun loginUseCase(userRemoteRepository: UserRemoteRepository, tokenRepository: TokenRepository) =
        LoginUseCase(userRemoteRepository, tokenRepository = tokenRepository)

    @Factory
    fun clearTokenUseCase(tokenRepository: TokenRepository) =
        ClearTokenUseCase(tokenRepository = tokenRepository)

    @KoinViewModel
    fun loginViewModel(
        loginUseCase: LoginUseCase,
        clearTokenUseCase: ClearTokenUseCase,
        emailValidateUseCase: EmailValidateUseCase,
        passwordValidateUseCase: PasswordValidateUseCase,
        updateFirstTimeCheckUseCase: UpdateFirstTimeCheckUseCase
    ) =
        LoginViewModel(
            loginUseCase = loginUseCase,
            clearTokenUseCase = clearTokenUseCase,
            emailValidateUseCase = emailValidateUseCase,
            passwordValidateUseCase = passwordValidateUseCase,
            updateFirstTimeCheckUseCase = updateFirstTimeCheckUseCase
        )
}