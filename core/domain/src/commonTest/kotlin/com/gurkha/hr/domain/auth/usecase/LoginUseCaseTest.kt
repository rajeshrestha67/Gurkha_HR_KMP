package com.gurkha.hr.domain.auth.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.FakeTokenRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.datastore.user_data.repository.FakeUserDataRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.auth.login.model.LoginData
import com.gurkha.hr.domain.auth.login.repository.FakeUserRemoteRepository
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.domain.auth.login.usecase.LoginUseCase
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.user_data.UserData
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

class LoginUseCaseTest : KoinTest {
    private val userRepo: UserRemoteRepository by inject()
    private val tokenRepo: TokenRepository by inject()
    private val userDataRepo: UserDataRepository by inject()
    private val useCase: LoginUseCase by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    singleOf(::FakeTokenRepository).bind<TokenRepository>()
                    singleOf(::FakeUserRemoteRepository).bind<UserRemoteRepository>()
                    singleOf(::FakeUserDataRepository).bind<UserDataRepository>()
                    singleOf(::LoginUseCase)
                }
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `login success should save token and userData`() = runTest {
        val result = useCase("user@example.com", "password123")

        result shouldBe ERPResult.Success(
            data = LoginData(
                "fakeToken",
                "HRM_EMPLOYEE",
                message = null
            )
        )

        val savedToken = tokenRepo.token.first()
        savedToken.jwtToken shouldBe "fakeToken"

        val savedUserData = userDataRepo.userDataFlow.first()
        savedUserData.email shouldBe "user@example.com"
    }

    @Test
    fun `login error should not update token or userData`() = runTest {

        val initialToken = tokenRepo.token.first()
        val initialUserData = userDataRepo.userDataFlow.first()

        (userRepo as FakeUserRemoteRepository).setShouldReturnError(true)
        val result = useCase("user@example.com", "password123")

        result shouldBe ERPResult.Error(
            error = DataError.NetworkError.Custom(
                "Simulated error"
            )
        )

        tokenRepo.token.first() shouldBe initialToken
        userDataRepo.userDataFlow.first() shouldBe initialUserData
    }

    @Test
    fun `login with empty username or password should return error`() = runTest {
        (userRepo as FakeUserRemoteRepository).setShouldReturnError(true)

        val result1 = useCase("", "password123")
        val result2 = useCase("user@example.com", "")

        result1 shouldBe ERPResult.Error(
            error = DataError.NetworkError.Custom("Simulated error")
        )
        result2 shouldBe ERPResult.Error(
            error = DataError.NetworkError.Custom("Simulated error")
        )
    }

    @Test
    fun `pre-populated token and userData are overwritten on success`() = runTest {
        // Pre-populate
        tokenRepo.saveToken(Token("oldToken"))
        userDataRepo.saveUserData(UserData(email = "old@example.com"))

        val result = useCase("newuser@example.com", "password123")
        result shouldBe ERPResult.Success(
            data = LoginData(
                token = "fakeToken",
                role = "HRM_EMPLOYEE",
                message = null
            )
        )

        // Token overwritten
        tokenRepo.token.first().jwtToken shouldBe "fakeToken"

        // UserData overwritten
        userDataRepo.userDataFlow.first().email shouldBe "newuser@example.com"
    }

    @Test
    fun `multiple sequential logins update repositories correctly`() = runTest {
        val result1 = useCase("user1@example.com", "password123")
        val result2 = useCase("user2@example.com", "password123")

        result1 shouldBe ERPResult.Success(LoginData("fakeToken", "HRM_EMPLOYEE", null))
        result2 shouldBe ERPResult.Success(LoginData("fakeToken", "HRM_EMPLOYEE", null))

        tokenRepo.token.first().jwtToken shouldBe "fakeToken"
        userDataRepo.userDataFlow.first().email shouldBe "user2@example.com"
    }
}