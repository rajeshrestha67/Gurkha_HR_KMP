package com.gurkha.hr.domain.leave.leaveRequest.usecase

import com.gurkha.hr.domain.leave.leaveRequest.mapper.toData
import com.gurkha.hr.domain.leave.leaveRequest.model.LeaveRequestData
import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onError

class LeaveRequestUseCase(
    private val leaveRemoteRepository: LeaveRemoteRepository
) {
    suspend operator fun invoke(
        startDate: String,
        endDate: String,
        leaveDuration: String,
        leaveTypeId: Int,
        reason: String,
        assigneeId : Int
    ): ERPResult<LeaveRequestData, DataError> {
        return leaveRemoteRepository.requestLeave(
            startDate = startDate,
            endDate = endDate,
            leaveDuration = leaveDuration,
            leaveTypeId = leaveTypeId,
            reason = reason,
            assigneeId = assigneeId
        ).map {
            it.toData()
        }
    }
}