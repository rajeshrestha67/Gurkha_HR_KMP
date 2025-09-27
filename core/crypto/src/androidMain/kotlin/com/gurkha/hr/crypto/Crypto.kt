package com.gurkha.hr.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

private const val MAX_POOL_SIZE = 10

object Crypto {
    private const val KEY_ALIAS = "secret"
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7

    private val keyStore = KeyStore
        .getInstance("AndroidKeyStore")
        .apply {
            load(null)
        }

    private fun getKey(): SecretKey {
        val existingKey = keyStore
            .getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator
            .getInstance(ALGORITHM)
            .apply {
                init(
                    KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or
                                KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(BLOCK_MODE)
                        .setEncryptionPaddings(PADDING)
                        .setRandomizedEncryptionRequired(true)
                        .setUserAuthenticationRequired(false)
                        .build()
                )
            }
            .generateKey()
    }

    private val cipherMutex = Mutex()
    private val cipherPool = mutableListOf<Cipher>()

    suspend fun safeEncrypt(bytes: ByteArray): ByteArray? {
        return try {
            encrypt(bytes)
        } catch (e: RuntimeException) {
            null
        }
    }

    private suspend fun encrypt(bytes: ByteArray): ByteArray {
        val cipher = getCipherFromPool()
        return try {
            cipher.init(Cipher.ENCRYPT_MODE, getKey())
            val iv = cipher.iv
            val encrypted = cipher.doFinal(bytes)
            iv + encrypted
        } catch (e: Exception) {
            throw RuntimeException("Encryption failed", e)
        } finally {
            returnCipherToPool(cipher)
        }
    }

    suspend fun safeDecrypt(encryptedBytes: ByteArray): ByteArray? {
        return try {
            decrypt(encryptedBytes)
        } catch (e: RuntimeException) {
            null
        }
    }

    private suspend fun decrypt(encryptedBytes: ByteArray): ByteArray {
        val cipher = getCipherFromPool()
        return try {

            val iv = encryptedBytes.copyOfRange(0, 16)

            val encryptedData = encryptedBytes.copyOfRange(16, encryptedBytes.size)

            cipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))

            cipher.doFinal(encryptedData)
        } catch (e: Exception) {
            throw RuntimeException("Decryption failed", e)
        } finally {
            returnCipherToPool(cipher)
        }
    }

    private suspend fun getCipherFromPool(): Cipher {
        return cipherMutex.withLock {
            if (cipherPool.isNotEmpty()) {
                cipherPool.removeAt(0)
            } else {
                Cipher.getInstance("AES/CBC/PKCS7Padding")
            }
        }
    }

    private suspend fun returnCipherToPool(cipher: Cipher) {
        cipherMutex.withLock {
            if (cipherPool.size < MAX_POOL_SIZE) {
                cipherPool.add(cipher)
            }
        }
    }
}