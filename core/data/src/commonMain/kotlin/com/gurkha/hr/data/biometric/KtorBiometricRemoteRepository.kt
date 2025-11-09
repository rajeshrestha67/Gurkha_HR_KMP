package com.gurkha.hr.data.biometric

import com.gurkha.hr.domain.biometric.repository.BiometricRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.biometric.BiometricRequestDto
import com.gurkha.model.biometric.BiometricResponseDto
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorBiometricRemoteRepository(
    private val httpClient: HttpClient
) : BiometricRemoteRepository {
    override suspend fun biometricRequest(
        biometricToken: String,
        uid: String
    ): ERPResult<BiometricResponseDto, DataError> {
        val requestDto =
            BiometricRequestDto(biometricToken = biometricToken, deviceUniqueIdentifier = uid)
//        AppLogger.i(
//            tag = "KtorBiometricRemoteRepository",
//            message = "biometric request ${Json.encodeToString(requestDto)}"
//        )
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.BIOMETRIC_REQUEST_END_POINT
            ) {
                setBody(
                    requestDto
                )
            }
        }
    }
}