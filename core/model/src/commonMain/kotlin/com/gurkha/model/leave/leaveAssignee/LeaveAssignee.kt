package com.gurkha.model.leave.leaveAssignee

import kotlinx.serialization.Serializable

@Serializable
data class LeaveAssigneeRequestDto(
    val branchId: Int? = null,
    val profileId: Int? = null
)

@Serializable
data class LeaveAssigneeResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<UserDetailDto>? = null,
    val success: Boolean? = null
)

@Serializable
data class UserDetailDto(
    val id: Int? = null,
    val fullName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val branchId: Int? = null,
    val profileId: Int? = null,
    val maxApprovalLimit: Int? = null,
    val allowAttendanceApproval: String? = null
)

