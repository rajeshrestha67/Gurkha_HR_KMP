package com.gurkha.hr.domain.userDetail.usecase

import com.gurkha.hr.datastore.user_data.model.UserData
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.mapper.toData
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.flow.firstOrNull



class FetchRemoteUserDetailUseCase(
    private val userDetailRemoteRepository: UserDetailRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(): ERPResult<UserDetailData, DataError> {
        return userDetailRemoteRepository.fetchUserDetail().map { it.toData() }
            .onSuccess { userDetail ->
                val userData = userDataRepository.userDataFlow.firstOrNull() ?: UserData()

                userDataRepository.saveUserData(userData.copy(
                    email = userDetail.email,
                    phoneNumber = userDetail.phoneNumber,
                    fullName =userDetail.fullName ,
                    levelName = userDetail.levelName,
                    employeeId = userDetail.employeeId.toString(),
                    branchName = userDetail.branchName,
                    joinedDate = userDetail.joinedDate,
                    address = userDetail.address,
                    dateOfBirth = userDetail.dateOfBirth,
                    gender = userDetail.gender,
                    nationality = userDetail.nationality,
                    maritalStatus = userDetail.maritalStatus,
                    guardianName = userDetail.guardianName,
                    guardianPhone = userDetail.guardianPhone
                ))
            }
    }
}
