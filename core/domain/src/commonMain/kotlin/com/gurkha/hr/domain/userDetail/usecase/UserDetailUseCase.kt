package com.gurkha.hr.domain.userDetail.usecase

import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.mapper.toData
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess

class UserDetailUseCase(
    private val userDetailRemoteRepository: UserDetailRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<UserDetailData, DataError>{
        return userDetailRemoteRepository.fetchUserDetail().map {
            it.toData()
        }
    }
}