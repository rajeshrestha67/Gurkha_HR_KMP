package com.gurkha.hr.datastore.token.local

import androidx.datastore.core.okio.OkioSerializer
import com.gurkha.hr.crypto.Cryptography
import com.gurkha.hr.datastore.token.model.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.use
import org.koin.mp.KoinPlatform.getKoin

internal object TokenJsonSerializer : OkioSerializer<Token> {
    val cryptography: Cryptography = getKoin().get()
    override val defaultValue: Token
        get() = Token()

    override suspend fun readFrom(source: BufferedSource): Token {
        val encryptedByte = withContext(Dispatchers.IO) {
            source.readByteArray()
        }
        return try {
            cryptography.decrypt(encryptedByte, Token.serializer())
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
                cryptography.encrypt(t, Token.serializer())?.let { data ->
                    it.write(data)
                }
            }
        }
    }
}