package com.gurkha.hr.datastore.notificationCount.local


import androidx.datastore.core.okio.OkioSerializer
import com.gurkha.hr.crypto.Cryptography
import com.gurkha.hr.datastore.notificationCount.model.NotificationTotalCountData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.use
import org.koin.mp.KoinPlatform.getKoin

internal object NotificationDataJsonSerializer : OkioSerializer<NotificationTotalCountData> {
    val cryptography: Cryptography = getKoin().get()
    override val defaultValue: NotificationTotalCountData
        get() = NotificationTotalCountData(0)


    //read the value from the source and then decrypt and if failed fallback to the default value
    override suspend fun readFrom(source: BufferedSource): NotificationTotalCountData {
        val encryptedByte = withContext(Dispatchers.IO) {
            source.readByteArray()
        }
        return try {
            cryptography.decrypt(encryptedByte, NotificationTotalCountData.serializer())
                ?: defaultValue
        } catch (_: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: NotificationTotalCountData,
        sink: BufferedSink
    ) {
        sink.use {
            withContext(Dispatchers.IO) {
                cryptography.encrypt(t, NotificationTotalCountData.serializer())?.let { data ->
                    it.write(data)
                }
            }
        }
    }

}