package com.gurkha.hr.domain.leaveType.usecase

import com.gurkha.hr.domain.leaveType.mapper.toData
import com.gurkha.hr.domain.leaveType.model.LeaveTypeData
import com.gurkha.hr.domain.leaveType.repository.LeaveTypeRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.ErrorData

class LeaveTypeUseCase(
    private val leaveTypeRemoteRepository: LeaveTypeRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<LeaveTypeData>, DataError>{
        return leaveTypeRemoteRepository.fetchLeaveType().map {
            it.toData()
        }
    }
}