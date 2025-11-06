package com.gurkha.hr.domain.biometric.mapper

import com.gurkha.hr.domain.biometric.model.BiometricData
import com.gurkha.model.biometric.BiometricResponseDto

fun BiometricResponseDto.toData(): BiometricData{
    return BiometricData(
        message = message ?: ""
    )
}