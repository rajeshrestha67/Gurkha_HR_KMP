package com.gurkha.hr.domain.form

import com.gurkha.hr.components.textField.ErrorStatus
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

class PasswordValidationUseCaseTest : KoinTest {
    val useCase: PasswordValidateUseCase by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    single { PasswordValidateUseCase() }
                }
            )
        }
    }

    @Test
    fun emptyPasswordShouldFailRequiredRule() {
        val password = ""
        val result = useCase(password)
        result shouldBe ErrorStatus(true, SharedRes.Strings.required)
    }

    @Test
    fun passwordLessThan6CharsShouldFailLengthRule() {
        val password = "12345"
        val result = useCase(password)
        result shouldBe ErrorStatus(true, SharedRes.Strings.invalidPasswordLength)
    }

    @Test
    fun passwordExactly6CharsShouldPass() {
        val password = "123456"
        val result = useCase(password)
        result shouldBe null
    }

    @Test
    fun longPasswordShouldPass() {
        val password = "averylongpassword123"
        val result = useCase(password)
        result shouldBe null
    }

    @Test
    fun passwordWithSpacesStillValidIfLengthOk() {
        val password = "abc 123"
        val result = useCase(password)
        result shouldBe null
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}
