package com.gurkha.di


import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.hr.domain.app.usecase.FetchUserInfoUseCase
import com.gurkha.hr.splashscreen.AppViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class AppModule {
    @Factory
    fun fetchUserThemeModeUseCase(
        userInfoRepository: UserInfoRepository
    ) = FetchUserInfoUseCase(userInfoRepository = userInfoRepository)


    @KoinViewModel
    fun appThemeViewModel(fetchUserThemeModeUseCase: FetchUserInfoUseCase) =
        AppViewModel(fetchUserThemeModeUseCase = fetchUserThemeModeUseCase)
}