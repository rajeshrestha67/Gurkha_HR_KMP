package com.gurkha.hr.domain.userDetail.repository

import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.userDetail.UserDetailResponseDto

interface UserDetailRemoteRepository {
    suspend fun fetchUserDetail() : ERPResult<UserDetailResponseDto, DataError>
}