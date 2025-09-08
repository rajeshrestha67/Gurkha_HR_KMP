package com.gurkha.hr.datastore

import com.gurkha.hr.datastore.token.local.TokenDataStore
import com.gurkha.hr.datastore.userInfo.local.UserInfoDataStore

expect class DataStoreFactory() {

    fun getSystemPath(jsonPath: String): String

    fun getTokenDataStore(jsonPath: String): TokenDataStore

    fun getUserInfo(jsonPath: String): UserInfoDataStore
}