package com.gurkha.hr.domain.leave.leaveReport.useCase

import com.gurkha.hr.domain.leave.leaveReport.mapper.toData
import com.gurkha.hr.domain.leave.leaveReport.model.LeaveReportData
import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class LeaveReportUseCase(
    private val leaveRemoteRepository: LeaveRemoteRepository
) {
    suspend operator fun invoke(
        leaveStatus: String,
        fromDate: String,
        toDate: String
    ): ERPResult<List<LeaveReportData>, DataError> {
        return leaveRemoteRepository.fetchLeaveReport(
            fromDate = fromDate,
            toDate = toDate,
            leaveStatus = leaveStatus
        ).map {
            it.toData()
        }
    }
}