package com.gurkha.di

import com.gurkha.hr.dashboard.DashboardViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class DashboardScreenModule {
    @KoinViewModel
    fun getDashboardViewModel() = DashboardViewModel()
    
}