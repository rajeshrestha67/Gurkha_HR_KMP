package com.gurkha.hr.domain.splash

import com.gurkha.hr.datastore.user_info.repository.FakeLocalUserInfoRepository
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
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

class UpdateFirstTimeCheckUseCaseTest : KoinTest {

    val updateFirstTimeCheckUseCase: UpdateFirstTimeCheckUseCase by inject()
    val repository: UserInfoRepository by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    singleOf(::FakeLocalUserInfoRepository).bind<UserInfoRepository>()
                    singleOf(::UpdateFirstTimeCheckUseCase)
                }
            )
        }
    }

    @Test
    fun `initial userInfoFlow should have null isFirstTime`() = runTest {
        val latest = repository.userInfoFlow.first()
        latest.isFirstTime shouldBe null
    }

    @Test
    fun `UpdateFirstTimeCheckUseCase should update isFirstTime to false`() = runTest {
        updateFirstTimeCheckUseCase()
        val latest = repository.userInfoFlow.first()
        latest.isFirstTime shouldBe false
    }


    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}