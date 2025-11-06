package com.gurkha.hr.datastore

import com.gurkha.hr.datastore.token.local.TokenDataStore
import com.gurkha.hr.datastore.user_data.local.UserDataDataStore
import com.gurkha.hr.datastore.user_info.local.UserInfoDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class DataStoreFactory {
    actual fun getSystemPath(jsonPath: String): String {
        return "${fileDirectory()}/$jsonPath.json"
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun fileDirectory(): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory).path!!
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