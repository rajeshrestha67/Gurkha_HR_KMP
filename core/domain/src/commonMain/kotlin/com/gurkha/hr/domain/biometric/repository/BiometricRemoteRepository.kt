package com.gurkha.hr.domain.biometric.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.biometric.BiometricResponseDto
import com.gurkha.model.network.DataError

interface BiometricRemoteRepository {
    suspend fun biometricRequest(
        biometricToken: String,
        uid: String
    ): ERPResult<BiometricResponseDto, DataError>
}