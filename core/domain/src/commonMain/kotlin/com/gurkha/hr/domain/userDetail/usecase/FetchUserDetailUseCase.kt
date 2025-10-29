package com.gurkha.hr.domain.userDetail.usecase

import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.userDetail.mapper.toData
import com.gurkha.hr.domain.userDetail.mapper.toDetail
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.DataError
import com.gurkha.model.user_data.UserData
import kotlinx.coroutines.flow.firstOrNull


class FetchUserDetailUseCase(
    private val userDetailRemoteRepository: UserDetailRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(force: Boolean = false): ERPResult<UserDetailData, DataError> {
        val userData = userDataRepository.userDataFlow.firstOrNull()

        return if (userData == null || force) {
            remoteFetch(userData)
        } else {
            ERPResult.Success(userData.toDetail())
        }
    }


    private suspend fun remoteFetch(userData: UserData?): ERPResult<UserDetailData, DataError> {
        return userDetailRemoteRepository.fetchUserDetail().map { it.toData() }
            .onSuccess { userDetail ->
                userDataRepository.saveUserData(
                    userData?.copy(
                        email = userDetail.email,
                        phoneNumber = userDetail.phoneNumber,
                        fullName = userDetail.fullName,
                        imageUrl = userDetail.userProfileUrl,
                        initials = userDetail.fullName.extractInitials(),
                        levelName = userDetail.levelName,
                        employeeId = userDetail.employeeId,
                        branchName = userDetail.branchName,
                        joinedDate = userDetail.joinedDate  ,
                        address = userDetail.address,
                        dateOfBirth = userDetail.dateOfBirth ,
                        gender = userDetail.gender,
                        nationality = userDetail.nationality,
                        maritalStatus = userDetail.maritalStatus,
                        guardianName = userDetail.guardianName,
                        guardianPhone = userDetail.guardianNumber,
                        pfNumber = userDetail.pfNumber,
                        panNumber = userDetail.panNumber,
                        isCompleteProfile = userDetail.isCompleteProfile
                    ) ?: UserData()
                )
            }

    }
}
