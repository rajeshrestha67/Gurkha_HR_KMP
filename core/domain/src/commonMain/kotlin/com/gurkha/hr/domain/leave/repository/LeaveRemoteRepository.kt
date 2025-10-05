package com.gurkha.hr.domain.leave.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.leaveAssignee.LeaveAssigneeResponseDto
import com.gurkha.model.leaveType.LeaveTypeResponseDto

interface LeaveRemoteRepository {
    suspend fun fetchLeaveType(): ERPResult<LeaveTypeResponseDto, DataError>
    suspend fun fetchAssignee(): ERPResult<LeaveAssigneeResponseDto, DataError>
}