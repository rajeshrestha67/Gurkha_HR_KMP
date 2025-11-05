package com.gurkha.hr.datastore

import android.content.Context
import com.gurkha.hr.datastore.token.local.TokenDataStore
import com.gurkha.hr.datastore.user_data.local.UserDataDataStore
import com.gurkha.hr.datastore.user_info.local.UserInfoDataStore
import org.koin.mp.KoinPlatform.getKoin

actual class DataStoreFactory {
    private val context: Context = getKoin().get()
    actual fun getSystemPath(jsonPath: String): String {
        return context.filesDir.resolve(
            "$jsonPath.json",
        ).absolutePath
    }

    actual fun getTokenDataStore(jsonPath: String): TokenDataStore {
        return TokenDataStore(
            produceFilePath = {
                getSystemPath(jsonPath)
            }
        )
    }

    actual fun getUserInfo(jsonPath: String): UserInfoDataStore {
        return UserInfoDataStore(
            produceFilePath = {
                getSystemPath(jsonPath)
            }
        )
    }

    actual fun getUserData(jsonPath: String): UserDataDataStore {
        return UserDataDataStore(
            produceFilePath = {
                getSystemPath(jsonPath)
            }
        )
    }


}