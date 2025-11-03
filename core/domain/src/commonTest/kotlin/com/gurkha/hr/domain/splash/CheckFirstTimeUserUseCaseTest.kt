package com.gurkha.hr.domain.splash

import com.gurkha.hr.datastore.user_info.repository.FakeLocalUserInfoRepository
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.model.user_info.UserInfo
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class CheckFirstTimeUserUseCaseTest : KoinTest {
    val checkFirstTimeUserUseCase: CheckFirstTimeUserUseCase by inject()
    val repository: UserInfoRepository by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    singleOf(::FakeLocalUserInfoRepository).bind<UserInfoRepository>()
                    singleOf(::CheckFirstTimeUserUseCase)
                }
            )
        }
    }

    @Test
    fun `first time user should return true`() = runTest {
        val latest = checkFirstTimeUserUseCase()
        latest shouldBe true
    }

    @Test
    fun `update first time user and should return false`() = runTest {
        val userInfo = UserInfo(isFirstTime = false)
        repository.saveUserInfo(userInfo)
        val latest = checkFirstTimeUserUseCase()
        latest shouldBe false
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}