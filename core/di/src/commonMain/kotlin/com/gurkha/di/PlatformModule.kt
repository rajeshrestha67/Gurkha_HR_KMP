package com.gurkha.di

import com.gurkha.hr.components.PlatformMessage
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
expect class PlatformModule() {

    @Single
    fun getPlatformMessage(): PlatformMessage

}