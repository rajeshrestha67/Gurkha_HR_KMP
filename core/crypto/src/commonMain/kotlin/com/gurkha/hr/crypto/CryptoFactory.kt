package com.gurkha.hr.crypto

expect object CryptoFactory {
    suspend inline fun <reified T> encrypt(t: T): ByteArray?
    suspend inline fun <reified T> decrypt(bytes: ByteArray): T?
}