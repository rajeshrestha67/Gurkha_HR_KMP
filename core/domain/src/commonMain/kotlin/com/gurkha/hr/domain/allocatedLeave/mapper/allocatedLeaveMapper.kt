package com.gurkha.hr.domain.allocatedLeave.mapper

import com.gurkha.hr.domain.allocatedLeave.model.AllocatedLeaveData
import com.gurkha.model.allocatedLeave.AllocatedLeaveResponseDto

fun AllocatedLeaveResponseDto.toData(): List<AllocatedLeaveData> {
    return detail?.map {
        AllocatedLeaveData(
            leaveType = it.leaveType?: "",
            totalDays = it.totalDaysAllowed?: 0.0,
            leaveTaken = it.totalDaysTaken?: 0.0,
            remainingLeave = it.totalDaysAssign?:0.0,
        )
}?: emptyList()}