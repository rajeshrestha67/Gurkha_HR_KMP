package com.gurkha.hr.datastore.userInfo.local

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.gurkha.hr.datastore.userInfo.model.UserInfo
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

    suspend fun save(userInfo: UserInfo) {
        db.updateData {_->
            UserInfo()
        }
    }
    //update the user data
    suspend fun update(userInfo: UserInfo) {
        db.updateData {current->
            current.copy(
                isFirstTime = userInfo.isFirstTime ?: current.isFirstTime
            )
        }
    }


}