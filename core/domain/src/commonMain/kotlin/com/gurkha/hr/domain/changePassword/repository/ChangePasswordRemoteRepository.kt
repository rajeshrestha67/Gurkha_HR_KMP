package com.gurkha.hr.domain.changePassword.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.changePassword.ChangePasswordResponseDTO
import com.gurkha.hr.domain.changePassword.mapper.toData


interface ChangePasswordRemoteRepository {
    suspend fun changePassword( newPassword: String,confirmPassword: String,): ERPResult<ChangePasswordResponseDTO, DataError>
}