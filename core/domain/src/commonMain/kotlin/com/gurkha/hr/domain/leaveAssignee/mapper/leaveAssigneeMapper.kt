package com.gurkha.hr.domain.leaveAssignee.mapper

import com.gurkha.hr.domain.leaveAssignee.model.LeaveAssigneeData
import com.gurkha.model.leaveAssignee.LeaveAssigneeResponseDto

fun LeaveAssigneeResponseDto.toData(): List<LeaveAssigneeData> {
    return detail?.map {
        LeaveAssigneeData(
            id = it.id ?: 0,
            fullName = it.fullName ?: "",
            email = it.email ?: "",
            phone = it.phone ?: "",
            branchId = it.branchId ?: 0,
            profileId = it.profileId ?: 0,
            maxApprovalLimit = it.maxApprovalLimit ?: 0,
            allowAttendanceApproval = it.allowAttendanceApproval ?: "",
        )
    } ?: emptyList()
}
