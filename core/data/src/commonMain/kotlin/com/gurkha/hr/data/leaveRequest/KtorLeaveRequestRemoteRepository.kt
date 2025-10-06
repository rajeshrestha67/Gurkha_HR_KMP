package com.gurkha.hr.data.leaveRequest

import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.leave.leaveAssignee.LeaveAssigneeResponseDto
import com.gurkha.model.leave.leaveReport.LeaveReportRequestDto
import com.gurkha.model.leave.leaveReport.LeaveReportResponseDto
import com.gurkha.model.leave.leaveRequest.LeaveRequestDto
import com.gurkha.model.leave.leaveRequest.LeaveRequestResponseDto
import com.gurkha.model.leave.leaveType.LeaveTypeResponseDto
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorLeaveRequestRemoteRepository(
    private val httpClient: HttpClient
) : LeaveRemoteRepository {
    override suspend fun fetchLeaveType(): ERPResult<LeaveTypeResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LEAVE_TYPE_END_POINT
            )
        }
    }

    override suspend fun fetchAssignee(): ERPResult<LeaveAssigneeResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LEAVE_ASSIGNEE_END_POINT
            )
        }
    }

    override suspend fun requestLeave(
        startDate: String,
        endDate: String,
        leaveDuration: String,
        leaveTypeId: Int,
        reason: String,
        assigneeId: Int
    ): ERPResult<LeaveRequestResponseDto, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LEAVE_REQUEST_END_POINT
            ) {
                setBody(
                    LeaveRequestDto(
                        startDate = startDate,
                        endDate = endDate,
                        leaveDuration = leaveDuration,
                        leaveTypeId = leaveTypeId,
                        reason = reason,
                        assigneeId = assigneeId
                    )
                )
            }
        }
    }

    override suspend fun fetchLeaveReport(
        leaveStatus: String
    ): ERPResult<LeaveReportResponseDto, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LEAVE_STATUS_REPORT_ENDPOINT
            ) {
                setBody(
                    LeaveReportRequestDto(
                        leaveStatus = leaveStatus
                    )
                )
            }
        }
    }
}