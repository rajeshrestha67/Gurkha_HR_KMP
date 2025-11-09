package com.gurkha.hr.domain.token.mapper

import com.gurkha.hr.domain.token.model.FcmTokenData
import com.gurkha.model.fcmToken.FcmTokenResponseDto

fun FcmTokenResponseDto.toData(): FcmTokenData{
    return FcmTokenData(
        message = message ?: ""
    )
}
