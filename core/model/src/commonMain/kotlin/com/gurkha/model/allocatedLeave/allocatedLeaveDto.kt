package com.gurkha.model.allocatedLeave

import kotlinx.serialization.Serializable

@Serializable
data class AllocatedLeaveResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<LeaveDetail>? = null,
    val success: Boolean? = null
)

@Serializable
data class LeaveDetail(
    val employeeId: Int? = null,
    val leaveTypeId: Int? = null,
    val leaveType: String? = null,
    val totalDaysAllowed: Double? = null,
    val totalDaysAssign: Double? = null,
    val totalDaysTaken: Double? = null,
    val substituteLeaveCount: Double? = null
)
