package com.gurkha.model.leave.leaveRequest

import kotlinx.serialization.Serializable

@Serializable
data class LeaveRequestDto(
    val endDate: String? = null,
    val leaveDuration: String? = null,
    val leaveTypeId: Int? = null,
    val reason: String? = null,
    val startDate: String? = null,
    val assigneeId: Int? = null
)


@Serializable
data class LeaveRequestResponseDto(
    val status: String? = null,
    val message: String? = null,
    val success: Boolean? = null
)
