package com.gurkha.di

import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.hr.domain.settings.usecase.UpdateUserThemeUseCase
import com.gurkha.hr.settings.SettingsViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class SettingsModule {

    @Factory
    fun updateUserThemeUseCase(
        userInfoRepository: UserInfoRepository
    ) = UpdateUserThemeUseCase(userInfoRepository = userInfoRepository)


    @KoinViewModel
    fun getSettingsViewModel(updateUserThemeUseCase: UpdateUserThemeUseCase) =
        SettingsViewModel(updateUserThemeUseCase = updateUserThemeUseCase)
}