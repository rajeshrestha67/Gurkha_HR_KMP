package com.gurkha.hr.datastore

import com.gurkha.hr.datastore.token.local.TokenDataStore

expect class DataStoreFactory() {

    fun getSystemPath(jsonPath: String): String

    fun getTokenDataStore(jsonPath: String): TokenDataStore
}