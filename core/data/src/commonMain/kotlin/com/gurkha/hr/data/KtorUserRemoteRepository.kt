package com.gurkha.hr.data

import com.gurkha.hr.domain.auth.login.UserRemoteRepository
import com.gurkha.hr.networkhelper.ERPError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorUserRemoteRepository(
    private val httpClient: HttpClient
): UserRemoteRepository {
    override suspend fun login(
        username: String,
        password: String
    ): ERPResult<String, ERPError> {
        return safeCall {  httpClient.get("https://www.google.com").body() }
    }
}