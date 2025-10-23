package com.gurkha.di

import com.gurkha.hr.data.app.UserThemeModeRepositoryImpl
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.hr.domain.app.repository.UserThemeModeRepository
import com.gurkha.hr.domain.app.usecase.FetchUserThemeModeUseCase
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class AppModule {
    @Factory(binds = [UserThemeModeRepository::class])
    fun getUserThemeModeRepository(
        userInfoRepository: UserInfoRepository
    ) = UserThemeModeRepositoryImpl(userInfoRepository)

    @Factory
    fun fetchUserThemeModeUseCase(
        userThemeModeRepository: UserThemeModeRepository
    ) = FetchUserThemeModeUseCase(userThemeModeRepository)


}