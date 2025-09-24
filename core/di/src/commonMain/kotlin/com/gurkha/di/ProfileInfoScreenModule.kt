package com.gurkha.di

import com.gurkha.hr.profile.profile_info.ProfileInfoScreenViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class ProfileInfoScreenModule {
    @KoinViewModel
    fun getProfileInfoScreenViewModel() = ProfileInfoScreenViewModel()


}