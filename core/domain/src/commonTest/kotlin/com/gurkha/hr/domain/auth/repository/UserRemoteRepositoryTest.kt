package com.gurkha.hr.domain.auth.repository

import com.gurkha.hr.domain.auth.login.repository.FakeUserRemoteRepository
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
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

class UserRemoteRepositoryTest : KoinTest {
    private val repository: UserRemoteRepository by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    singleOf(::FakeUserRemoteRepository).bind<UserRemoteRepository>()
                }
            )
        }
    }

    @Test
    fun `login returns success ERPResult`() = runTest {
        val result = repository.login("user", "pass")

        when (result) {
            is ERPResult.Success -> result.data.token shouldBe "fakeToken"
            else -> error("Expected success result but got $result")
        }
    }

    @Test
    fun `login returns error ERPResult`() = runTest {
        val fakeRepo = repository as FakeUserRemoteRepository
        fakeRepo.setShouldReturnError(true)

        val result = repository.login("user", "pass")

        when (result) {
            is ERPResult.Error -> result.error shouldBe DataError.NetworkError.Custom("Simulated error")
            else -> error("Expected error result but got $result")
        }
    }

    @Test
    fun `login returns network error ERPResult`() = runTest {
        val fakeRepo = repository as FakeUserRemoteRepository
        fakeRepo.setShouldReturnNetworkError(true)

        val result = repository.login("user", "pass")

        when (result) {
            is ERPResult.Error -> result.error shouldBe DataError.NetworkError.DataUnknown
            else -> error("Expected network error but got $result")
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}