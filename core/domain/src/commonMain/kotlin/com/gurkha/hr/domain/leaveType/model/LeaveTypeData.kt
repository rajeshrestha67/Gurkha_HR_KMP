package com.gurkha.hr.domain.leaveType.model

data class LeaveTypeData(
    val id: Int,
    val createdDate: String,
    val createdByUserId: Int,
    val createdBy: String,
    val modifiedBy: String,
    val lastModified: String,
    val typeName: String,
    val maxDaysAllowed: Int,
    val active: String,
    val accumulate: String,
    val enableCountWeekend: String,
    val enableCountHoliday: String
)
