package com.gurkha.hr.domain.token.usecase

import com.gurkha.hr.domain.token.mapper.toData
import com.gurkha.hr.domain.token.model.FcmTokenData
import com.gurkha.hr.domain.token.repository.FcmTokenRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class PostFcmTokenUseCase(
    private val fcmTokenRepository: FcmTokenRepository
) {
    suspend operator fun invoke(
        fcmToken: String,
        uid: String
    ): ERPResult<FcmTokenData, DataError>{
        return fcmTokenRepository.postFcmToken(
            fcmToken = fcmToken,
            uid = uid
        ).map {
            it.toData()
        }
    }
}