package com.gurkha.di

import com.gurkha.hr.dashboard.DashboardViewModel
import com.gurkha.hr.data.userDetail.KtorUserDetailRemoteRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.domain.userDetail.usecase.FetchRemoteUserDetailUseCase
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
    fun userDetailUseCase(
        userDetailRemoteRepository: UserDetailRemoteRepository,
        userDataRepository: UserDataRepository
    ): FetchRemoteUserDetailUseCase =
        FetchRemoteUserDetailUseCase(
            userDetailRemoteRepository,
            userDataRepository
        )


    @KoinViewModel
    fun getDashboardViewModel(
        userDetailUseCase: FetchRemoteUserDetailUseCase
    ) = DashboardViewModel(
        userDetailUseCase = userDetailUseCase
    )

}