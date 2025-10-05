package com.gurkha.di

import com.gurkha.hr.network.HttpClientEngineFactory
import com.gurkha.model.AuthState
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.mp.KoinPlatform.getKoin

@Module
class NetworkModule {
    @Single
    fun httpClient(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.SIMPLE
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    encodeDefaults = true
                    isLenient = true
                    allowSpecialFloatingPointValues = true
                    allowStructuredMapKeys = true
                    prettyPrint = false
                    useArrayPolymorphism = false
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
        install(ResponseObserver) {
            onResponse { response ->
                if (response.status.value == 401) {
                    val authState: AuthState = getKoin().get()
                    authState.logout()
                }
            }
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    @Factory
    fun httpClientEngine(): HttpClientEngine = HttpClientEngineFactory().getHttpEngine()

    @Single
    fun getAuthState(): AuthState = AuthState()

}

