package com.gurkha.di

import com.gurkha.hr.datastore.userInfo.repository.UserInfoRepository
import com.gurkha.hr.domain.splash.CheckFirstTimeUserUseCase
import com.gurkha.hr.splashscreen.SplashscreenViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class SplashScreenModule {

    @KoinViewModel
    fun getSplashScreenViewModel(checkFirstTimeUserUseCase: CheckFirstTimeUserUseCase) = SplashscreenViewModel(checkFirstTimeUserUseCase = checkFirstTimeUserUseCase)

    @Factory
    fun getFirstTimeUserUseCase(userInfoRepository: UserInfoRepository) =
        CheckFirstTimeUserUseCase(userInfoRepository = userInfoRepository)
}