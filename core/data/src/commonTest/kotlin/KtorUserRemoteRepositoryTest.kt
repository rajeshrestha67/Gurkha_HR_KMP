import com.gurkha.hr.data.login.KtorUserRemoteRepository
import com.gurkha.hr.datastore.token.repository.FakeTokenRepository
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.model.auth.login.LoginRequestDto
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
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

class KtorUserRemoteRepositoryTest : KoinTest {

    private val repository: UserRemoteRepository by inject()

    @BeforeTest
    fun setup() {
        val mockEngine = MockEngine { request ->
            if (request.url.encodedPath.endsWith(EndPoint.LOGIN_END_POINT)) {
                val bodyText = request.body.toByteArray().decodeToString()
                val loginRequest = Json.decodeFromString<LoginRequestDto>(bodyText)
                when {
                    loginRequest.email != "valid@gmail.com" -> {
                        respond(
                            content = """{"message": "invalid email"}""",
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }

                    loginRequest.password != "password" -> {
                        respond(
                            content = """{"message": "invalid password"}""",
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }

                    else -> {
                        respond(
                            content = """{"token": "fakeToken","role": "HRM_EMPLOYEE"}""",
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }
                }

            } else {
                respondBadRequest()
            }
        }
        startKoin {
            modules(
                module {
                    single {
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                json(
                                    Json {
                                        ignoreUnknownKeys = true
                                        isLenient = true
                                    }
                                )
                            }
                        }
                    }
                    singleOf(::KtorUserRemoteRepository).bind<UserRemoteRepository>()
                    singleOf(::FakeTokenRepository).bind<TokenRepository>()
                }
            )
        }
    }

    @Test
    fun `Login returns success ERPResult`() = runTest {
        val result = repository.login("valid@gmail.com", "password")
        when (result) {
            is ERPResult.Success -> result.data.token shouldBe "fakeToken"
            else -> error("Expected success")
        }
    }

    @Test
    fun `Login returns invalid email ERPResult`() = runTest {
        val result = repository.login("notvalid@gmail.com", "password")
        when (result) {
            is ERPResult.Success -> result.data.message shouldBe "invalid email"
            is ERPResult.Error -> error("Expected success")
        }
    }

    @Test
    fun `Login returns empty email ERPResult`() = runTest {
        val result = repository.login("", "password")
        when (result) {
            is ERPResult.Success -> result.data.message shouldBe "invalid email"
            is ERPResult.Error -> error("Expected success")
        }
    }

    @Test
    fun `Login returns invalid password ERPResult`() = runTest {
        val result = repository.login("valid@gmail.com", "notPassword")
        when (result) {
            is ERPResult.Success -> result.data.message shouldBe "invalid password"
            is ERPResult.Error -> error("Expected success")
        }
    }

    @Test
    fun `Login returns empty password ERPResult`() = runTest {
        val result = repository.login("valid@gmail.com", "")
        when (result) {
            is ERPResult.Success -> result.data.message shouldBe "invalid password"
            is ERPResult.Error -> error("Expected success")
        }
    }

    @AfterTest
    fun tearDown() = runBlocking {
        stopKoin()
    }
}