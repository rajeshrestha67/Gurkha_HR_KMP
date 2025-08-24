package com.gurkha.hr.data

import com.gurkha.hr.domain.auth.login.UserRemoteRepository
import com.gurkha.model.auth.login.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorUserRemoteRepository(
    private val httpClient: HttpClient
): UserRemoteRepository {
    override suspend fun login(
        username: String,
        password: String
    ): Result<String> {
        return httpClient.get("https://www.google.com").body()
    }
}