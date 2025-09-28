package com.gurkha.hr.data.leaveAssignee

import com.gurkha.hr.domain.leaveAssignee.repository.LeaveAssigneeRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.leaveAssignee.LeaveAssigneeResponseDto
import io.ktor.client.HttpClient

class KtorLeaveAssigneeRemoteRepository(
    private val httpClient: HttpClient
) : LeaveAssigneeRemoteRepository {
    override suspend fun fetchAssignee(): ERPResult<LeaveAssigneeResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LEAVE_ASSIGNEE_END_POINT
            )
        }
    }
}