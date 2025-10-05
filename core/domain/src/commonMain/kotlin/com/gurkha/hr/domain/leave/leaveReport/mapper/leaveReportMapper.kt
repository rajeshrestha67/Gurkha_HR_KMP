package com.gurkha.hr.domain.leave.leaveReport.mapper

import com.gurkha.hr.domain.leave.leaveReport.model.LeaveReportData
import com.gurkha.model.leave.leaveReport.LeaveReportResponseDto

fun LeaveReportResponseDto.toData(): List<LeaveReportData> {
    return detail?.leaveRequests?.map { item ->
        LeaveReportData(
            employeeId = item.employeeId ?: 0,
            startDate = item.startDate ?: "",
            endDate = item.endDate ?: "",
            leaveStatus = item.leaveStatus ?: "",
            reason = item.reason ?: "",
            leaveDuration = item.leaveDuration ?: "",
            assigneeName = item.assigneeName ?: "",
            totalDays = item.totalDays ?: 0.0,
            leaveType = item.leaveType ?: "",
            requestedDate = item.requestedDate ?:""
        )
    } ?: emptyList()
}
