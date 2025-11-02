package com.gurkha.hr.crypto

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.refTo
import kotlinx.cinterop.value
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import platform.CoreCrypto.CCCrypt
import platform.CoreCrypto.kCCAlgorithmAES
import platform.CoreCrypto.kCCBlockSizeAES128
import platform.CoreCrypto.kCCDecrypt
import platform.CoreCrypto.kCCEncrypt
import platform.CoreCrypto.kCCOptionPKCS7Padding
import platform.CoreCrypto.kCCSuccess
import platform.darwin.ByteVar
import platform.posix.size_tVar

class IOSCryptography : Cryptography {

    suspend fun safeEncrypt(bytes: ByteArray): ByteArray? {
        return try {
            encrypt(
                data = bytes,
                key = key,
                iv = iv
            )
        } catch (e: RuntimeException) {
            null
        }
    }

    suspend fun safeDecrypt(encryptedBytes: ByteArray): ByteArray? {
        return try {
            decrypt(
                data = encryptedBytes,
                key = key,
                iv = iv
            )
        } catch (e: RuntimeException) {
            null
        }
    }


    fun encrypt(data: ByteArray, key: ByteArray, iv: ByteArray): ByteArray {
        return crypt(data, key, iv, kCCEncrypt.toInt())
    }


    fun decrypt(data: ByteArray, key: ByteArray, iv: ByteArray): ByteArray {
        return crypt(data, key, iv, kCCDecrypt.toInt())
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun crypt(data: ByteArray, key: ByteArray, iv: ByteArray, operation: Int): ByteArray {
        memScoped {
            val dataLength = data.size.toULong()
            val bufferSize = data.size + kCCBlockSizeAES128.toInt()
            val buffer = allocArray<ByteVar>(bufferSize)

            val numBytesOut = alloc<size_tVar>()

            val status = CCCrypt(
                operation.toUInt(),
                algorithm,
                options,
                key.refTo(0), key.size.convert(),
                iv.refTo(0),
                data.refTo(0), dataLength,
                buffer, bufferSize.convert(),
                numBytesOut.ptr
            )

            if (status != kCCSuccess) {
                throw IllegalStateException("Error: $status")
            }

            return buffer.readBytes(numBytesOut.value.toInt())
        }
    }

    override suspend fun <T> encrypt(
        t: T,
        serializer: KSerializer<T>
    ): ByteArray? {
        val str = Json.encodeToString(serializer, t)
        val bytes = str.encodeToByteArray()
        return safeEncrypt(bytes)
    }

    override suspend fun <T> decrypt(
        bytes: ByteArray,
        deserializer: KSerializer<T>
    ): T? {
        val decryptedBytes = safeDecrypt(bytes)
        val json = decryptedBytes?.decodeToString()
        return json?.let { Json.decodeFromString(deserializer, it) }
    }

    companion object {
        private const val algorithm = kCCAlgorithmAES
        private const val options = kCCOptionPKCS7Padding // CBC is default if no ECB set
        private val key = ByteArray(16) { 0x01 } // 128-bit key
        private val iv = ByteArray(16) { 0x02 }  // 16-byte IV
    }
}

actual fun getPlatformCryptography(): Cryptography {
    return IOSCryptography()
}