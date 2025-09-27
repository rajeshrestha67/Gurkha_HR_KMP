package com.gurkha.hr.domain.form

import com.gurkha.hr.res.SharedRes
import io.kotest.matchers.shouldBe
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class RequiredValidationUseCaseTest : KoinTest {

    val useCase: RequiredValidationUseCase by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    single { RequiredValidationUseCase() }
                }
            )
        }
    }

    @Test
    fun emptyValueShouldFailRequiredRule() {
        val value = ""
        val result = useCase(value)
        result shouldBe SharedRes.Strings.required
    }

    @Test
    fun notEmptyValueShouldPassRule() {
        val value = "test"
        val result = useCase(value)
        result shouldBe null
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}