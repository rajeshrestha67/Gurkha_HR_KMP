package com.gurkha.hr.domain.biometric.useCase

import com.gurkha.hr.domain.biometric.mapper.toData
import com.gurkha.hr.domain.biometric.model.BiometricData
import com.gurkha.hr.domain.biometric.repository.BiometricRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class BiometricRequestUseCase(
    private val biometricRemoteRepository: BiometricRemoteRepository
) {
    suspend operator fun invoke(
        biometricToken: String,
        uid: Int
    ): ERPResult<BiometricData, DataError>{
        return biometricRemoteRepository.biometricRequest(
            biometricToken= biometricToken,
            uid = uid
        ).map {
            it.toData()
        }
    }
}