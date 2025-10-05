package com.gurkha.hr.domain.leave.leaveAssignee.model

import com.gurkha.model.leave.ui.LeaveAssigneeUi

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

fun List<LeaveAssigneeData>.toUiList(): List<LeaveAssigneeUi> =
    map {
        LeaveAssigneeUi(
            name = it.fullName,
            value = it.id.toString()
        )
    }