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
    fun `Empty Email Should Fail Required Rule`() {
        val email = ""
        val result = useCase(email)
        result shouldBe SharedRes.Strings.required
    }

    @Test
    fun `Missing At Symbol Should Fail Email Rule`() {
        val email = "testexample.com"
        val result = useCase(email)
        result shouldBe SharedRes.Strings.invalidEmailAddress
    }

    @Test
    fun `Missing Domain Should Fail Email Rule`() {
        val email = "test@"
        val result = useCase(email)
        result shouldBe SharedRes.Strings.invalidEmailAddress
    }

    @Test
    fun `Missing Username Should Fail Email Rule`() {
        val email = "@example.com"
        val result = useCase(email)
        result shouldBe SharedRes.Strings.invalidEmailAddress
    }

    @Test
    fun `Valid Simple Email Should Pass`() {
        val email = "test@example.com"
        val result = useCase(email)
        result shouldBe null
    }

    @Test
    fun `Valid Email With Subdomain Should Pass`() {
        val email = "user@mail.example.co.uk"
        val result = useCase(email)
        result shouldBe null
    }

    @Test
    fun `Valid Email With Special Characters Should Pass`() {
        val email = "user.name+tag@example.com"
        val result = useCase(email)
        result shouldBe null
    }
}
