package com.gurkha.hr.domain.allocatedLeave.repository


import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.allocatedLeave.AllocatedLeaveResponseDto
import com.gurkha.model.network.DataError

interface AllocatedLeaveRemoteRepository {
    suspend fun getAllocatedLeave(): ERPResult<AllocatedLeaveResponseDto, DataError>

}