package com.gurkha.hr.domain.userDetail.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.userDetail.mapper.toData
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class UpdateUserDetailUseCase(
    private val userDetailRemoteRepository: UserDetailRemoteRepository,

){
    suspend operator fun invoke(data: UserDetailData): ERPResult<UserDetailData, DataError> {
        return userDetailRemoteRepository.userDataUpdate(
            data = data
        ).map {
            it.toData()
        }
    }
}