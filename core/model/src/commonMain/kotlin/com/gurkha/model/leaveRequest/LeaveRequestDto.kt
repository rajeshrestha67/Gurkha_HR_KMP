package com.gurkha.model.leaveRequest

import kotlinx.serialization.Serializable

@Serializable
data class LeaveRequestDtoTypeResponseDto(
    val status: String? = null,
    val message: String? = null,
//    val detail: List<LeaveTypeDetailDto>? = null,
    val success: Boolean? = null
)

//@Serializable
//data class LeaveTypeDetailDto(
//    val id: Int? = null,
//    val createdDate: String? = null,
//    val createdByUserId: Int? = null,
//    val createdBy: String? = null,
//    val modifiedBy: String? = null,
//    val lastModified: String? = null,
//    val typeName: String? = null,
//    val maxDaysAllowed: Int? = null,
//    val active: String? = null,
//    val accumulate: String? = null,
//    val enableCountWeekend: String? = null,
//    val enableCountHoliday: String? = null
//)

