package com.gurkha.hr.datastore.token.local

import androidx.datastore.core.okio.OkioSerializer
import com.gurkha.hr.crypto.CryptoFactory
import com.gurkha.hr.datastore.token.model.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.use

internal object TokenJsonSerializer : OkioSerializer<Token> {
    override val defaultValue: Token
        get() = Token()

    override suspend fun readFrom(source: BufferedSource): Token {
        val encryptedByte = withContext(Dispatchers.IO) {
            source.readByteArray()
        }
        return try {
            CryptoFactory.decrypt(encryptedByte)
                ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: Token,
        sink: BufferedSink
    ) {
        sink.use {
            withContext(Dispatchers.IO) {
                CryptoFactory.encrypt(t)?.let { data ->
                    it.write(data)
                }
            }
        }
    }
}