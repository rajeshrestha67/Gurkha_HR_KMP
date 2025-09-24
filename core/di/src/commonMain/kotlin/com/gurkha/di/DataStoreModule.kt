package com.gurkha.di

import com.gurkha.hr.datastore.DataStoreFactory
import com.gurkha.hr.datastore.token.local.TokenDataStore
import com.gurkha.hr.datastore.token.repository.LocalTokenRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.datastore.user_data.local.UserDataDataStore
import com.gurkha.hr.datastore.user_data.repository.LocalUserDataRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.datastore.user_info.local.UserInfoDataStore
import com.gurkha.hr.datastore.user_info.repository.LocalUserInfoRepository
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
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

    @Single
    fun getUserDataDataStore(): UserDataDataStore {
        val factory: DataStoreFactory = getKoin().get()
        return factory.getUserData("user_data")
    }


    @Factory(binds = [TokenRepository::class])
    fun getTokenRepository(tokenDataStore: TokenDataStore) = LocalTokenRepository(tokenDataStore)

    @Factory(binds = [UserInfoRepository::class])
    fun getUserInfoRepository(userInfoDataStore: UserInfoDataStore) =
        LocalUserInfoRepository(userInfoDataStore)

    @Factory(binds = [UserDataRepository::class])
    fun getUserInfoRepository(userDataDataStore: UserDataDataStore) =
        LocalUserDataRepository(userDataDataStore)

    @Single
    fun getDataStoreFactory(): DataStoreFactory = DataStoreFactory()


}
