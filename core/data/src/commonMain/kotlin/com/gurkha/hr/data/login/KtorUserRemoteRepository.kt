package com.gurkha.hr.data.login

import com.gurkha.hr.components.device_info.getDeviceInfo
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.auth.login.LoginRequestDto
import com.gurkha.model.auth.login.LoginResponseDto
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorUserRemoteRepository(
    private val httpClient: HttpClient
) : UserRemoteRepository {
    override suspend fun login(
        username: String,
        password: String
    ): ERPResult<LoginResponseDto, DataError> {
        val deviceInfo = getDeviceInfo("10")
        val request = LoginRequestDto(
            email = username,
            password = password,
            deviceInfo = deviceInfo
        )
        AppLogger.i(TAG, "login: api request $request")
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LOGIN_END_POINT
            ) {
                setBody(request)
            }
        }
    }

    companion object {
        private const val TAG = "KtorUserRemoteRepository"

    }
}