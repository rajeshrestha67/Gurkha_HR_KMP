package com.gurkha.hr.domain.leave.leaveType.model

import com.gurkha.model.leave.ui.LeaveTypeUi

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


fun List<LeaveTypeData>.toUiList(): List<LeaveTypeUi> =
    map {
        LeaveTypeUi(
            name = it.typeName,
            value = it.id.toString()
        )
    }