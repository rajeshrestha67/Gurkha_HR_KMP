package com.gurkha.hr.domain.changePassword.usecase

import com.gurkha.hr.domain.changePassword.model.ChangePasswordData
import com.gurkha.hr.domain.changePassword.repository.ChangePasswordRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.domain.changePassword.mapper.toData

import com.gurkha.hr.networkhelper.ERPError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map

class ChangePasswordUseCase(
    private  val changePasswordRemoteRepository: ChangePasswordRemoteRepository,
)
{
    suspend operator fun invoke(
        newPassword: String,
        confirmPassword: String,
    ): ERPResult<ChangePasswordData, DataError>{
        return changePasswordRemoteRepository
            .changePassword(newPassword, confirmPassword)
            .map{ it.toData() }
    }

}