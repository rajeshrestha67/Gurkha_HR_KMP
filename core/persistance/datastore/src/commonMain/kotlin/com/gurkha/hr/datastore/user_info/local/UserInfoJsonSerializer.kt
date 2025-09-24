package com.gurkha.hr.datastore.user_info.local

import androidx.datastore.core.okio.OkioSerializer
import com.gurkha.hr.crypto.CryptoFactory

import com.gurkha.hr.datastore.user_info.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.use


internal object UserInfoJsonSerializer : OkioSerializer<UserInfo> {
    override val defaultValue: UserInfo
        get() = UserInfo()


    //read the value from the source and then decrypt and if failed fallback to the default value
    override suspend fun readFrom(source: BufferedSource): UserInfo {
        val encryptedByte = withContext(Dispatchers.IO) {
            source.readByteArray()
        }
        return try {
            CryptoFactory.decrypt(encryptedByte) ?: defaultValue
        } catch (ex: Exception) {
            defaultValue
        }
    }

    //    decrypt the value and save it to the sink(source)
    override suspend fun writeTo(
        t: UserInfo,
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