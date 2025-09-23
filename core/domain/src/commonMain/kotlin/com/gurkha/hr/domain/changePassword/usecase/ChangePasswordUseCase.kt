package com.gurkha.hr.domain.changePassword.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.changePassword.mapper.toData
import com.gurkha.hr.domain.changePassword.model.ChangePasswordData
import com.gurkha.hr.domain.changePassword.repository.ChangePasswordRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import kotlinx.coroutines.flow.firstOrNull

class ChangePasswordUseCase(
    private val changePasswordRemoteRepository: ChangePasswordRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(
        newPassword: String,
        confirmPassword: String,
    ): ERPResult<ChangePasswordData, DataError> {
        val email = userDataRepository.userDataFlow.firstOrNull()?.email ?: ""
        return changePasswordRemoteRepository
            .changePassword(
                email = email,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            ).map { it.toData() }
    }

}