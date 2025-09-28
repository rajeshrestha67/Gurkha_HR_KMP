package com.gurkha.hr.domain.leaveType.mapper

import com.gurkha.hr.domain.leaveType.model.LeaveTypeData
import com.gurkha.model.leaveType.LeaveTypeResponseDto

fun LeaveTypeResponseDto.toData(): List<LeaveTypeData> {
    return detail?.map {
        LeaveTypeData(
            id = it.id ?: 0,
            createdDate = it.createdDate ?: "",
            createdByUserId = it.createdByUserId ?: 0,
            createdBy = it.createdBy ?: "",
            modifiedBy = it.modifiedBy ?: "",
            lastModified = it.lastModified ?: "",
            typeName = it.typeName ?: "",
            maxDaysAllowed = it.maxDaysAllowed ?: 0,
            active = it.active ?: "",
            accumulate = it.accumulate ?: "",
            enableCountWeekend = it.enableCountWeekend ?: "",
            enableCountHoliday = it.enableCountHoliday ?: "",
        )
    } ?: emptyList()
}