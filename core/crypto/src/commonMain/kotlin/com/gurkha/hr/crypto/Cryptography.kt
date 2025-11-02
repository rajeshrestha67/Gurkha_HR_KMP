package com.gurkha.hr.crypto

import kotlinx.serialization.KSerializer

interface Cryptography {
    suspend fun <T> encrypt(t: T, serializer: KSerializer<T>): ByteArray?
    suspend fun <T> decrypt(bytes: ByteArray, deserializer: KSerializer<T>): T?
}

expect fun getPlatformCryptography(): Cryptography