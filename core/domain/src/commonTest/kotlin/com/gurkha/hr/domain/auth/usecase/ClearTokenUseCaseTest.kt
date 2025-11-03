package com.gurkha.hr.domain.auth.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.FakeTokenRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
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

class ClearTokenUseCaseTest : KoinTest {

    val tokenRepository: TokenRepository by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    singleOf(::FakeTokenRepository).bind<TokenRepository>()
                }
            )
        }
    }

    @Test
    fun `initial token emits default Token`() = runTest {
        val token = tokenRepository.token.first()
        token shouldBe Token()
    }

    @Test
    fun `saveToken updates the token flow`() = runTest {
        val newToken = Token()
        tokenRepository.saveToken(newToken)
        val emitted = tokenRepository.token.first()
        emitted shouldBe newToken
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}