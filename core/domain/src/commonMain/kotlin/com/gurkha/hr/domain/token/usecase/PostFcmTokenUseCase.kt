package com.gurkha.hr.domain.token.usecase

import com.gurkha.hr.components.device_info.getDeviceUniqueIdentifier
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.domain.token.mapper.toData
import com.gurkha.hr.domain.token.model.FcmTokenData
import com.gurkha.hr.domain.token.repository.FcmTokenRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull


class PostFcmTokenUseCase(
    private val fcmTokenRepository: FcmTokenRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(
    ): ERPResult<FcmTokenData, DataError> {

        val fcmToken = tokenRepository.token.firstOrNull()?.fcmToken ?: ""
        return fcmTokenRepository.postFcmToken(
            fcmToken = fcmToken,
            uid = getDeviceUniqueIdentifier()
        ).map {
            it.toData()
        }
    }
}