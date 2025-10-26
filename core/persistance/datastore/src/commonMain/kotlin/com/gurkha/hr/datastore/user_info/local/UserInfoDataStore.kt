package com.gurkha.hr.datastore.user_info.local

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.gurkha.model.user_info.UserInfo

import kotlinx.coroutines.flow.Flow
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM


class UserInfoDataStore(
    private val produceFilePath: () -> String
) {

    //    create db for the user info
    private val db = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = UserInfoJsonSerializer,
            producePath = {
                produceFilePath().toPath()
            }
        )
    )

    //get the userdata
    val userInfoFlow: Flow<UserInfo>
        get() = db.data

    suspend fun update(userInfo: UserInfo) {
        db.updateData { _ ->
            userInfo
        }
    }
}