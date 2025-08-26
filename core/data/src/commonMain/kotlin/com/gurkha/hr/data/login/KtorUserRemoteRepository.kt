package com.gurkha.hr.data.login

import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.domain.auth.login.model.LoginData
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.auth.login.LoginRequestDto
import com.gurkha.model.auth.login.LoginResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorUserRemoteRepository(
    private val httpClient: HttpClient
) : UserRemoteRepository {
    override suspend fun login(
        username: String,
        password: String
    ): ERPResult<LoginResponseDto, ERPError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LOGIN_END_POINT
            ) {
                setBody(LoginRequestDto(username, password))
            }
        }
    }
}