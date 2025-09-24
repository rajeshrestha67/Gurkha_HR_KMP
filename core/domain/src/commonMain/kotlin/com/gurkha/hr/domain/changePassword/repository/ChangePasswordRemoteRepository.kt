package com.gurkha.hr.domain.changePassword.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.changePassword.ChangePasswordResponseDTO


interface ChangePasswordRemoteRepository {
    suspend fun changePassword(
        email: String,
        newPassword: String,
        confirmPassword: String,
    ): ERPResult<ChangePasswordResponseDTO, DataError>
}