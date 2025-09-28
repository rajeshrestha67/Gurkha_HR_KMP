package com.gurkha.hr.domain.leaveAssignee.usecase

import com.gurkha.hr.domain.leaveAssignee.mapper.toData
import com.gurkha.hr.domain.leaveAssignee.model.LeaveAssigneeData
import com.gurkha.hr.domain.leaveAssignee.repository.LeaveAssigneeRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map

class LeaveAssigneeUseCase(
    private val leaveAssigneeRemoteRepository: LeaveAssigneeRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<LeaveAssigneeData>, DataError> {
        return leaveAssigneeRemoteRepository.fetchAssignee().map {
            it.toData()
        }
    }
}