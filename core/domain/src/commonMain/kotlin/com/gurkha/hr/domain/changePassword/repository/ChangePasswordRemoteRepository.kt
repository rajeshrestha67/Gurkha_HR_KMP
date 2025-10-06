package com.gurkha.hr.domain.changePassword.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.changePassword.ChangePasswordResponseDTO
import com.gurkha.model.network.DataError


interface ChangePasswordRemoteRepository {
    suspend fun changePassword(
        email: String,
        newPassword: String,
        confirmPassword: String,
    ): ERPResult<ChangePasswordResponseDTO, DataError>
}