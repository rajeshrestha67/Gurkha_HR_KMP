package com.gurkha.hr.domain.leave.leaveRequest.mapper

import com.gurkha.hr.domain.leave.leaveRequest.model.LeaveRequestData
import com.gurkha.model.leave.leaveRequest.LeaveRequestResponseDto

fun LeaveRequestResponseDto.toData(): LeaveRequestData {
    return LeaveRequestData(
        status = status ?: "",
        message = message ?: "",
        success = success ?: true
    )
}