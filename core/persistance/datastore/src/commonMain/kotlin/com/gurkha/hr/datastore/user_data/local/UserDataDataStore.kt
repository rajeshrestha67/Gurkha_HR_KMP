package com.gurkha.hr.datastore.user_data.local

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.gurkha.hr.datastore.user_data.model.UserData
import kotlinx.coroutines.flow.Flow
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

class UserDataDataStore(
    private val produceFilePath: () -> String
) {
    private val db = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = UserDataJsonSerializer,
            producePath = {
                produceFilePath().toPath()
            }
        )
    )

    //get the userdata
    val userInfoFlow: Flow<UserData>
        get() = db.data

    suspend fun update(userData: UserData) {
        db.updateData { _ ->
            userData
        }
    }
}
