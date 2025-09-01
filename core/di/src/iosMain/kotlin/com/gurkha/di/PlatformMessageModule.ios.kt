package com.gurkha.di

import com.gurkha.hr.components.PlatformMessage
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual class PlatformMessageModule actual constructor() {
    @Single
    actual fun getPlatformMessage(): PlatformMessage {
        return PlatformMessage()
    }
}