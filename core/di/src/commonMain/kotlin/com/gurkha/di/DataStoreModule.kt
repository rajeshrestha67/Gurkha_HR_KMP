package com.gurkha.di

import com.gurkha.hr.datastore.DataStoreFactory
import com.gurkha.hr.datastore.token.local.TokenDataStore
import com.gurkha.hr.datastore.token.repository.LocalTokenRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.datastore.userInfo.local.UserInfoDataStore
import com.gurkha.hr.datastore.userInfo.repository.LocalUserInfoRepository
import com.gurkha.hr.datastore.userInfo.repository.UserInfoRepository
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

    @Single
    fun getUserInfoDataStore(): UserInfoDataStore {
        val factory: DataStoreFactory = getKoin().get()
        return factory.getUserInfo("userInfo")
    }


    @Factory(binds = [TokenRepository::class])
    fun getTokenRepository(tokenDataStore: TokenDataStore) = LocalTokenRepository(tokenDataStore)

    @Factory(binds = [UserInfoRepository:: class])
    fun getUserInfoRepository(userInfoDataStore: UserInfoDataStore) = LocalUserInfoRepository(userInfoDataStore)
    @Single
    fun getDataStoreFactory(): DataStoreFactory = DataStoreFactory()


}
