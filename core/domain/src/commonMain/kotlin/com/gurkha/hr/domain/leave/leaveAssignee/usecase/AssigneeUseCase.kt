package com.gurkha.hr.domain.leave.leaveAssignee.usecase

import com.gurkha.hr.domain.leave.leaveAssignee.mapper.toData
import com.gurkha.hr.domain.leave.leaveAssignee.model.LeaveAssigneeData
import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class AssigneeUseCase(
    private val leaveRemoteRepository: LeaveRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<LeaveAssigneeData>, DataError> {
        return leaveRemoteRepository.fetchAssignee().map {
            it.toData()
        }
    }
}