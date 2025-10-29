package com.gurkha.hr.domain.userDetail.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.userDetail.mapper.toData
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.model.UserUpdateData
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.DataError
import com.gurkha.model.userDetail.UpdateRequestUserDto
import com.gurkha.model.user_data.UserData
import kotlinx.coroutines.flow.firstOrNull

class UpdateUserDetailUseCase(
    private val userDetailRemoteRepository: UserDetailRemoteRepository,
    private val userDataRepository: UserDataRepository

    ) {
    suspend operator fun invoke(data: UpdateRequestUserDto): ERPResult<UserUpdateData, DataError> {
        return userDetailRemoteRepository.userDataUpdate(
            data = data
        ).map {
            it.toData()
        }.onSuccess {
            val userData = userDataRepository.userDataFlow.firstOrNull() ?: UserData()

            userDataRepository.saveUserData(userData.copy(
                pfNumber = data.pfNumber ?: "",
                panNumber = data.panNumber ?: "",
                joinedDate = data.joinedDate ?: "",
                dateOfBirth = data.startDate ?: "",
                bloodGroup = data.bloodGroup ?: "",
                guardianName = data.guardianName ?: "",
                guardianPhone = data.guardianNumber ?: "",
                employeeTypes = data.employeeType ?: "",
//                temp solution
                isCompleteProfile = "Y"
            ))
        }
    }
}