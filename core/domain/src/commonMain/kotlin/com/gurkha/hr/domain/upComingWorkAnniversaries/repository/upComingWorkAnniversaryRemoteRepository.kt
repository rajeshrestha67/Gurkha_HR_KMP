package com.gurkha.hr.domain.upComingWorkAnniversaries.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.upComingWorkAnniversaries.UpComingWorkAnniversaryDetailDto

interface UpComingWorkAnniversaryRemoteRepository {
    suspend fun fetchUpComingWorkAnniversary(): ERPResult<UpComingWorkAnniversaryDetailDto, DataError>
}