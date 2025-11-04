package com.gurkha.di

import com.gurkha.hr.components.platform_utils.PlatformUtils
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
expect class PlatformUtilsModule() {
    @Single
    fun providePlatformUtils(): PlatformUtils
}