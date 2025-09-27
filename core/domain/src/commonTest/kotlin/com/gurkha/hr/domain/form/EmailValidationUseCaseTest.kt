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

class EmailValidationUseCaseTest : KoinTest {

    private val useCase: EmailValidateUseCase by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    single { EmailValidateUseCase() }
                }
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun emptyEmailShouldFailRequiredRule() {
        val email = ""
        val result = useCase(email)
        result shouldBe SharedRes.Strings.required
    }

    @Test
    fun missingAtSymbolShouldFailEmailRule() {
        val email = "testexample.com"
        val result = useCase(email)
        result shouldBe SharedRes.Strings.invalidEmailAddress
    }

    @Test
    fun missingDomainShouldFailEmailRule() {
        val email = "test@"
        val result = useCase(email)
        result shouldBe SharedRes.Strings.invalidEmailAddress
    }

    @Test
    fun missingUsernameShouldFailEmailRule() {
        val email = "@example.com"
        val result = useCase(email)
        result shouldBe SharedRes.Strings.invalidEmailAddress
    }

    @Test
    fun validSimpleEmailShouldPass() {
        val email = "test@example.com"
        val result = useCase(email)
        result shouldBe null
    }

    @Test
    fun validEmailWithSubdomainShouldPass() {
        val email = "user@mail.example.co.uk"
        val result = useCase(email)
        result shouldBe null
    }

    @Test
    fun validEmailWithSpecialCharactersShouldPass() {
        val email = "user.name+tag@example.com"
        val result = useCase(email)
        result shouldBe null
    }
}
