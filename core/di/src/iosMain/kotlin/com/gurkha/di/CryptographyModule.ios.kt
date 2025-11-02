package com.gurkha.di

import com.gurkha.hr.crypto.Cryptography
import com.gurkha.hr.crypto.getPlatformCryptography
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual class CryptographyModule {
    @Single
    actual fun getCryptography(): Cryptography {
        return getPlatformCryptography()
    }
}