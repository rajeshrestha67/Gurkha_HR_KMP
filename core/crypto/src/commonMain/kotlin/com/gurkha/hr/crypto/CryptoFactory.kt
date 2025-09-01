package com.gurkha.hr.crypto

import kotlinx.serialization.json.Json

object CryptoFactory {
    suspend inline fun <reified T> encrypt(t: T): ByteArray? {
        val str = Json.encodeToString(t)
        return str.encodeToByteArray()
    }

    suspend inline fun <reified T> decrypt(bytes: ByteArray): T? {
        val json = bytes.decodeToString()
        return Json.decodeFromString(json)
    }
}