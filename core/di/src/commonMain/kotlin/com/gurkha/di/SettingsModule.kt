package com.gurkha.di

import com.gurkha.hr.data.settings.UpdateUserThemeRepositoryImpl
import com.gurkha.hr.domain.app.repository.UserThemeModeRepository
import com.gurkha.hr.domain.settings.repository.UpdateUserThemeRepository
import com.gurkha.hr.domain.settings.usecase.UpdateUserThemeUseCase
import com.gurkha.hr.settings.SettingsViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class SettingsModule {
    @Factory(binds = [UpdateUserThemeRepository::class])
    fun updateUserThemeRepository(
        userThemeModeRepository: UserThemeModeRepository
    ) = UpdateUserThemeRepositoryImpl(userThemeModeRepository = userThemeModeRepository)

    @Factory
    fun updateUserThemeUseCase(
        updateUserThemeRepository: UpdateUserThemeRepository
    ) = UpdateUserThemeUseCase(updateUserThemeRepository = updateUserThemeRepository)


    @KoinViewModel
    fun getSettingsViewModel(updateUserThemeUseCase: UpdateUserThemeUseCase) =
        SettingsViewModel(updateUserThemeUseCase = updateUserThemeUseCase)
}