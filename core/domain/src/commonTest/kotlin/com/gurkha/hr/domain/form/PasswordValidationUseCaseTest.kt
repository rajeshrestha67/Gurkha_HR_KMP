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
    fun `Empty Password Should Fail Required Rule`() {
        val password = ""
        val result = useCase(password)
        result shouldBe SharedRes.Strings.required
    }

    @Test
    fun `Password Less Than 6 Chars Should Fail Length Rule`() {
        val password = "12345"
        val result = useCase(password)
        result shouldBe SharedRes.Strings.invalidPasswordLength
    }

    @Test
    fun `Password Exactly 6 Chars Should Pass`() {
        val password = "123456"
        val result = useCase(password)
        result shouldBe null
    }

    @Test
    fun `Long Password Should Pass`() {
        val password = "averylongpassword123"
        val result = useCase(password)
        result shouldBe null
    }

    @Test
    fun `Password With Spaces Still Valid If Length Ok`() {
        val password = "abc 123"
        val result = useCase(password)
        result shouldBe null
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}
