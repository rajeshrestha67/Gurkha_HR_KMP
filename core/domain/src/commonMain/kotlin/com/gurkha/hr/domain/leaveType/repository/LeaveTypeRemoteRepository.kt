package com.gurkha.hr.domain.leaveType.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.leaveType.LeaveTypeResponseDto

interface LeaveTypeRemoteRepository {
    suspend fun fetchLeaveType(): ERPResult<LeaveTypeResponseDto, DataError>
}