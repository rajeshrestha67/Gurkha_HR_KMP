package com.gurkha.hr.domain.token.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.fcmToken.FcmTokenResponseDto
import com.gurkha.model.network.DataError

interface FcmTokenRepository {
    suspend fun postFcmToken(
        uid: String,
        fcmToken: String
    ): ERPResult<FcmTokenResponseDto, DataError>
}