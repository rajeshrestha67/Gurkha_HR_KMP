package com.gurkha.hr.data.firebase

import com.gurkha.hr.domain.token.repository.FcmTokenRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.fcmToken.FcmTokenRequestDto
import com.gurkha.model.fcmToken.FcmTokenResponseDto
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorFcmTokenRemoteRepository(
    private val httpClient: HttpClient
) : FcmTokenRepository {
    override suspend fun postFcmToken(
        uid: String,
        fcmToken: String
    ): ERPResult<FcmTokenResponseDto, DataError> {
        val fcmTokenRequest = FcmTokenRequestDto(
            uid = uid,
            fcmToken = fcmToken
        )
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.POST_FCM_TOKEN_END_POINT
            ) {
                setBody(fcmTokenRequest)
            }
        }
    }
}