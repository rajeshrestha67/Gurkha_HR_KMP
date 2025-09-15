package com.gurkha.di

import com.gurkha.hr.profile.ProfileScreenViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class ProfileScreenModule {
   @KoinViewModel
   fun getProfileScreenViewModel() = ProfileScreenViewModel()
}