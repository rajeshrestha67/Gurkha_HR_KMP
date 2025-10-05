package com.gurkha.hr.domain.leaveAssignee.usecase

import com.gurkha.hr.domain.leaveAssignee.mapper.toData
import com.gurkha.hr.domain.leaveAssignee.model.LeaveAssigneeData
import com.gurkha.hr.domain.leave.repository.LeaveRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map

class LeaveAssigneeUseCase(
    private val leaveRemoteRepository: LeaveRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<LeaveAssigneeData>, DataError> {
        return leaveRemoteRepository.fetchAssignee().map {
            it.toData()
        }
    }
}