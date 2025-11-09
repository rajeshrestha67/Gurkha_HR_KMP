package com.gurkha.di

import com.gurkha.hr.dashboard.DashboardViewModel
import com.gurkha.hr.data.firebase.KtorFcmTokenRemoteRepository
import com.gurkha.hr.data.userDetail.KtorUserDetailRemoteRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.token.repository.FcmTokenRepository
import com.gurkha.hr.domain.token.usecase.FetchTokenAllValueUseCase
import com.gurkha.hr.domain.token.usecase.FetchTokenUseCase
import com.gurkha.hr.domain.token.usecase.PostFcmTokenUseCase
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.model.AuthState
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class DashboardScreenModule {

    @Factory(binds = [UserDetailRemoteRepository::class])
    fun userDetailRemoteRepository(httpClient: HttpClient) =
        KtorUserDetailRemoteRepository(httpClient)

    @Factory(binds = [FcmTokenRepository::class])
    fun fcmTokenRepository(httpClient: HttpClient) =
        KtorFcmTokenRemoteRepository(httpClient)

    @Factory
    fun postFcmTokenUseCase(
        fcmTokenRepository :FcmTokenRepository
    ): PostFcmTokenUseCase= PostFcmTokenUseCase(
        fcmTokenRepository = fcmTokenRepository
    )

    @Factory
    fun userDetailUseCase(
        userDetailRemoteRepository: UserDetailRemoteRepository,
        userDataRepository: UserDataRepository
    ): FetchUserDetailUseCase =
        FetchUserDetailUseCase(
            userDetailRemoteRepository,
            userDataRepository
        )

    @Factory
    fun fetchTokenUseCase(tokenRepository: TokenRepository): FetchTokenUseCase =
        FetchTokenUseCase(tokenRepository = tokenRepository)

    @KoinViewModel
    fun getDashboardViewModel(
        userDetailUseCase: FetchUserDetailUseCase,
        authState: AuthState,
        fetchTokenUseCase: FetchTokenUseCase,
        postFcmTokenUseCase: PostFcmTokenUseCase,
        fetchTokenAllValueUseCase: FetchTokenAllValueUseCase
    ) = DashboardViewModel(
        userDetailUseCase = userDetailUseCase,
        authState = authState,
        fetchTokenUseCase = fetchTokenUseCase,
        postFcmTokenUseCase = postFcmTokenUseCase,
        fetchTokenAllValueUseCase=fetchTokenAllValueUseCase
    )

}