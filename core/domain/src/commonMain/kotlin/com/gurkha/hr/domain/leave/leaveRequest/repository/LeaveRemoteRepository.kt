package com.gurkha.hr.domain.leave.leaveRequest.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.leave.leaveAssignee.LeaveAssigneeResponseDto
import com.gurkha.model.leave.leaveReport.LeaveReportResponseDto
import com.gurkha.model.leave.leaveRequest.LeaveRequestResponseDto
import com.gurkha.model.leave.leaveType.LeaveTypeResponseDto

interface LeaveRemoteRepository {
    suspend fun fetchLeaveType(): ERPResult<LeaveTypeResponseDto, DataError>
    suspend fun fetchAssignee(): ERPResult<LeaveAssigneeResponseDto, DataError>

    suspend fun requestLeave(
        startDate: String,
        endDate: String,
        leaveDuration: String,
        leaveTypeId: Int,
        reason: String,
        assigneeId: Int
    ): ERPResult<LeaveRequestResponseDto, DataError>

    suspend fun fetchLeaveReport(
        leaveStatus: String
    ): ERPResult<LeaveReportResponseDto, DataError>

}