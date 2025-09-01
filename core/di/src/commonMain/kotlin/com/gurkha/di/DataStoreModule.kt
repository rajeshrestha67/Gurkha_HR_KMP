package com.gurkha.di

import com.gurkha.hr.datastore.DataStoreFactory
import com.gurkha.hr.datastore.token.local.TokenDataStore
import com.gurkha.hr.datastore.token.repository.LocalTokenRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.mp.KoinPlatform.getKoin

@Module
class DataStoreModule {

    @Single
    fun getTokenDataStore(): TokenDataStore {
        val factory: DataStoreFactory = getKoin().get()
        return factory.getTokenDataStore("token")
    }


    @Factory(binds = [TokenRepository::class])
    fun getTokenRepository(tokenDataStore: TokenDataStore) = LocalTokenRepository(tokenDataStore)

    @Single
    fun getDataStoreFactory(): DataStoreFactory = DataStoreFactory()
}
