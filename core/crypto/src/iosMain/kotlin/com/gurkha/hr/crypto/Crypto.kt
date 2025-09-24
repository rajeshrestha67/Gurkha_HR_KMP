package com.gurkha.hr.crypto

//import platform.CommonCrypto.*
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreCrypto.CCCrypt
import platform.CoreCrypto.CCOperation
import platform.CoreCrypto.kCCAlgorithmAES
import platform.CoreCrypto.kCCBlockSizeAES128
import platform.CoreCrypto.kCCDecrypt
import platform.CoreCrypto.kCCEncrypt
import platform.CoreCrypto.kCCKeySizeAES256
import platform.CoreCrypto.kCCOptionPKCS7Padding
import platform.CoreCrypto.kCCSuccess
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.errSecSuccess
import platform.Security.kSecAttrApplicationTag
import platform.Security.kSecClass
import platform.Security.kSecClassKey
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import platform.posix.arc4random_buf
import platform.posix.memcpy
import platform.posix.size_tVar

private const val KEY_ALIAS = "secret"
private const val KEY_SIZE = kCCKeySizeAES256 // 256-bit AES key
private const val BLOCK_SIZE = kCCBlockSizeAES128 // AES block size (16 bytes)

@OptIn(ExperimentalForeignApi::class)
object Crypto {
    // Store key in Keychain

    @OptIn(ExperimentalForeignApi::class)
    private fun getKey(): ByteArray {
        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassKey,
            kSecAttrApplicationTag to KEY_ALIAS,
            kSecReturnData to kCFBooleanTrue!!

        )

        memScoped {
            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query as CFDictionaryRef, result.ptr)
            if (status == errSecSuccess) {
                val nsData = result.value as NSData
                return nsData.toByteArray()
            }
        }

        // If not found → create a new one
        return createKey()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun createKey(): ByteArray {
        val key = ByteArray(KEY_SIZE.toInt()) { (0..255).random().toByte() }

        val nsData = key.toNSData()
        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassKey,
            kSecAttrApplicationTag to KEY_ALIAS,
            kSecValueData to nsData
        )

        SecItemAdd(query as CFDictionaryRef, null)
        return key
    }

    fun safeEncrypt(data: ByteArray): ByteArray? {
        return try {
            encrypt(data)
        } catch (e: Throwable) {
            null
        }
    }

    fun safeDecrypt(data: ByteArray): ByteArray? {
        return try {
            decrypt(data)
        } catch (e: Throwable) {
            null
        }
    }

    private fun encrypt(data: ByteArray): ByteArray {
        val key = getKey()
        val iv = randomIV()
        val encrypted = crypt(data, key, iv, kCCEncrypt)
        return iv + encrypted // prepend IV like Android version
    }

    private fun decrypt(data: ByteArray): ByteArray {
        val key = getKey()
        val iv = data.copyOfRange(0, BLOCK_SIZE.toInt())
        val encrypted = data.copyOfRange(BLOCK_SIZE.toInt(), data.size)
        return crypt(encrypted, key, iv, kCCDecrypt)
    }

    private fun crypt(
        input: ByteArray,
        key: ByteArray,
        iv: ByteArray,
        operation: CCOperation
    ): ByteArray {
        memScoped {
            val outLength = alloc<size_tVar>()
            val outBytes = ByteArray(input.size + BLOCK_SIZE.toInt())

            val status = CCCrypt(
                operation,
                kCCAlgorithmAES,
                kCCOptionPKCS7Padding,
                key.refTo(0), key.size.convert(),
                iv.refTo(0),
                input.refTo(0), input.size.convert(),
                outBytes.refTo(0), outBytes.size.convert(),
                outLength.ptr
            )

            if (status != kCCSuccess) {
                throw RuntimeException("CCCrypt failed with status $status")
            }

            return outBytes.copyOf(outLength.value.toInt())
        }
    }

    private fun randomIV(): ByteArray {
        val iv = ByteArray(BLOCK_SIZE.toInt())
        arc4random_buf(iv.refTo(0), iv.size.convert())
        return iv
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray =
    ByteArray(length.toInt()).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytes, length)
        }
    }

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData =
    NSData.create(bytes = this.refTo(0) as COpaquePointer?, length = size.toULong())