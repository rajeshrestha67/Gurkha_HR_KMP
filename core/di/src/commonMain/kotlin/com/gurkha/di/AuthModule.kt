package com.gurkha.di

import com.gurkha.hr.data.KtorUserRemoteRepository
import com.gurkha.hr.domain.auth.login.UserRemoteRepository
import com.gurkha.hr.login.LoginViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class AuthModule {


    @Factory(binds = [UserRemoteRepository::class])
    fun userRepository(httpClient: HttpClient) = KtorUserRemoteRepository(httpClient)

    @KoinViewModel
    fun loginViewModel(userRemoteRepository: UserRemoteRepository) = LoginViewModel(userRemoteRepository)
}