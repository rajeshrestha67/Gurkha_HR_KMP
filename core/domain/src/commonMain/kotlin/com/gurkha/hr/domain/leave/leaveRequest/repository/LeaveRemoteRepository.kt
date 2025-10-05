package com.gurkha.hr.domain.leave.leaveRequest.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.leave.leaveAssignee.LeaveAssigneeResponseDto
import com.gurkha.model.leave.leaveType.LeaveTypeResponseDto

interface LeaveRemoteRepository {
    suspend fun fetchLeaveType(): ERPResult<LeaveTypeResponseDto, DataError>
    suspend fun fetchAssignee(): ERPResult<LeaveAssigneeResponseDto, DataError>
}