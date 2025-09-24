package com.gurkha.hr.crypto

import kotlinx.serialization.json.Json

actual object CryptoFactory {
    actual suspend inline fun <reified T> encrypt(t: T): ByteArray? {
        val str = Json.encodeToString(t)
        val bytes = str.encodeToByteArray()
        return Crypto.safeEncrypt(bytes)
    }

    actual suspend inline fun <reified T> decrypt(bytes: ByteArray): T? {
        val decryptedBytes = Crypto.safeDecrypt(bytes)
        val json = decryptedBytes?.decodeToString()
        return json?.let { Json.decodeFromString(it) }
    }
}