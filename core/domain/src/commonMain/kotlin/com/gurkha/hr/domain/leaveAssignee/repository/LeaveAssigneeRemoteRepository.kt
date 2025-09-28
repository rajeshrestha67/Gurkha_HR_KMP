package com.gurkha.hr.domain.leaveAssignee.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.leaveAssignee.LeaveAssigneeResponseDto

interface LeaveAssigneeRemoteRepository {
    suspend fun fetchAssignee(): ERPResult<LeaveAssigneeResponseDto, DataError>
}