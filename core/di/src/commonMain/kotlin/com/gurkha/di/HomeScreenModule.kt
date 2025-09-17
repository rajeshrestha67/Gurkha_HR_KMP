package com.gurkha.di

import com.gurkha.hr.home.HomeScreenViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class HomeScreenModule {
    @KoinViewModel
    fun getHomeScreenViewModel()= HomeScreenViewModel()
}