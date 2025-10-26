package com.gurkha.hr.domain.upComingBirthday.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.upComingBirthday.UserUpComingBirthdayDetailDto

interface UpComingBirthdayRemoteRepository {
    suspend fun fetchUpComingBirthday(): ERPResult<UserUpComingBirthdayDetailDto, DataError>
}