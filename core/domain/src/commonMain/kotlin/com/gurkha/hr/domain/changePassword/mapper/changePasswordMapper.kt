package com.gurkha.hr.domain.changePassword.mapper

import com.gurkha.hr.domain.changePassword.model.ChangePasswordData
import com.gurkha.model.changePassword.ChangePasswordResponseDTO

fun ChangePasswordResponseDTO.toData(): ChangePasswordData{
    return ChangePasswordData(
        status = status ?: "",
        message = message,
        success = success,
    )
}