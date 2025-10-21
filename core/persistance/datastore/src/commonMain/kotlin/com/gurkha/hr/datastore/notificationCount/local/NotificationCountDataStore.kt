package com.gurkha.hr.datastore.notificationCount.local

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.gurkha.hr.datastore.notificationCount.model.NotificationTotalCountData
import kotlinx.coroutines.flow.Flow
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

class NotificationCountDataStore(
    private val produceFilePath: () -> String
) {
    private val db = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = NotificationDataJsonSerializer,
            producePath = {
                produceFilePath().toPath()
            }
        )
    )

    //get notification count
    val notificationFlow: Flow<NotificationTotalCountData>
        get() = db.data

    suspend fun update(notificationTotalCountData: NotificationTotalCountData) {
        db.updateData { _ ->
            notificationTotalCountData
        }
    }
}