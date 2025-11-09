package com.gurkha.di

import com.gurkha.hr.data.biometric.KtorBiometricRemoteRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.hr.domain.biometric.repository.BiometricRemoteRepository
import com.gurkha.hr.domain.biometric.useCase.BiometricRequestUseCase
import com.gurkha.hr.domain.settings.usecase.UpdateUserLanguageUseCase
import com.gurkha.hr.domain.settings.usecase.UpdateUserThemeUseCase
import com.gurkha.hr.domain.token.usecase.FetchTokenAllValueUseCase
import com.gurkha.hr.domain.token.usecase.UpdateBiometricEnableUseCase
import com.gurkha.hr.domain.token.usecase.UpdateBiometricTokenUseCase
import com.gurkha.hr.settings.SettingsViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class SettingsModule {

    @Factory(binds = [BiometricRemoteRepository::class])
    fun biometricRemoteRepository(httpClient: HttpClient): BiometricRemoteRepository =
        KtorBiometricRemoteRepository(httpClient = httpClient)


    @Factory
    fun updateUserThemeUseCase(
        userInfoRepository: UserInfoRepository
    ) = UpdateUserThemeUseCase(userInfoRepository = userInfoRepository)


    @Factory
    fun updateUserLanguageUseCase(userInfoRepository: UserInfoRepository) =
        UpdateUserLanguageUseCase(userInfoRepository = userInfoRepository)


    @Factory
    fun fetchBiometricEnableUseCase(
        tokenRepository: TokenRepository,
    ): FetchTokenAllValueUseCase = FetchTokenAllValueUseCase(
        tokenRepository = tokenRepository
    )

    @Factory
    fun updateBiometricEnableUseCase(
        tokenRepository: TokenRepository
    ): UpdateBiometricEnableUseCase = UpdateBiometricEnableUseCase(
        tokenRepository = tokenRepository
    )

    @Factory
    fun updateBiometricTokenUseCase(
        tokenRepository: TokenRepository
    ): UpdateBiometricTokenUseCase = UpdateBiometricTokenUseCase(
        tokenRepository = tokenRepository
    )

    @Factory
    fun biometricRequestUseCase(
        biometricRemoteRepository: BiometricRemoteRepository
    ): BiometricRequestUseCase = BiometricRequestUseCase(
        biometricRemoteRepository = biometricRemoteRepository
    )


    @Factory
    @KoinViewModel
    fun getSettingsViewModel(
        updateUserThemeUseCase: UpdateUserThemeUseCase,
        updateUserLanguageUseCase: UpdateUserLanguageUseCase,
        fetchTokenAllValueUseCase: FetchTokenAllValueUseCase,
        updateBiometricEnableUseCase: UpdateBiometricEnableUseCase,
        biometricRequestUseCase: BiometricRequestUseCase
    ) =
        SettingsViewModel(
            fetchTokenAllValueUseCase = fetchTokenAllValueUseCase,
            updateBiometricEnableUseCase = updateBiometricEnableUseCase,
            updateUserThemeUseCase = updateUserThemeUseCase,
            updateUserLanguageUseCase = updateUserLanguageUseCase,
            biometricRequestUseCase = biometricRequestUseCase
        )
}