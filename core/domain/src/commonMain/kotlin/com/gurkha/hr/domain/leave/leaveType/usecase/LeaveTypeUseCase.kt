package com.gurkha.hr.domain.leave.leaveType.usecase

import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import com.gurkha.hr.domain.leave.leaveType.mapper.toData
import com.gurkha.hr.domain.leave.leaveType.model.LeaveTypeData
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map

class LeaveTypeUseCase(
    private val leaveRemoteRepository: LeaveRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<LeaveTypeData>, DataError>{
        return leaveRemoteRepository.fetchLeaveType().map {
            it.toData()
        }
    }
}