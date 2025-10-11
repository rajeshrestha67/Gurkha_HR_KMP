package com.gurkha.hr.domain.allocatedLeave.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.allocatedLeave.AllocatedLeaveResponseDto

interface AllocatedLeaveRemoteRepository {
    suspend fun getAllocatedLeave(): ERPResult<AllocatedLeaveResponseDto, DataError>

}