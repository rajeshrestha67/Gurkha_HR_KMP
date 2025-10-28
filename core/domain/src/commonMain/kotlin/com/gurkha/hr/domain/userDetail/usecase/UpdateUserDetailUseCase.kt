package com.gurkha.hr.domain.userDetail.usecase

import com.gurkha.hr.domain.userDetail.mapper.toData
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.model.UserUpdateData
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import com.gurkha.model.userDetail.UpdateRequestUserDto

class UpdateUserDetailUseCase(
    private val userDetailRemoteRepository: UserDetailRemoteRepository,

    ) {
    suspend operator fun invoke(data: UpdateRequestUserDto): ERPResult<UserUpdateData, DataError> {
        return userDetailRemoteRepository.userDataUpdate(
            data = data
        ).map {
            it.toData()
        }
    }
}