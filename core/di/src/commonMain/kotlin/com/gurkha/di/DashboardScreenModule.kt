package com.gurkha.di

import com.gurkha.hr.dashboard.DashboardViewModel
import com.gurkha.hr.data.userDetail.KtorUserDetailRemoteRepository
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.domain.userDetail.usecase.UserDetailUseCase
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class DashboardScreenModule {

    @Factory(binds = [UserDetailRemoteRepository::class])
    fun userDetailRemoteRepository(httpClient: HttpClient) =
        KtorUserDetailRemoteRepository(httpClient)

    @Factory
    fun userDetailUseCase(userDetailRemoteRepository: UserDetailRemoteRepository): UserDetailUseCase =
        UserDetailUseCase(
            userDetailRemoteRepository
        )

    @KoinViewModel
    fun getDashboardViewModel(
        userDetailUseCase: UserDetailUseCase
    ) = DashboardViewModel(
        userDetailUseCase = userDetailUseCase
    )

}