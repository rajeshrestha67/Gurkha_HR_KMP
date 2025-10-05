package com.gurkha.hr.domain.leave.leaveAssignee.model

data class LeaveAssigneeData(
    val id: Int,
    val fullName: String,
    val email: String,
    val phone: String,
    val branchId: Int,
    val profileId: Int,
    val maxApprovalLimit: Int,
    val allowAttendanceApproval: String
)
