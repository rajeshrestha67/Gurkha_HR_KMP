package com.gurkha.hr.domain.leave.leaveSummary.useCase

import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import com.gurkha.hr.domain.leave.leaveSummary.mappper.toData
import com.gurkha.hr.domain.leave.leaveSummary.model.LeaveSummaryData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class LeaveSummaryUseCase(
    private val leaveRemoteRepository: LeaveRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<LeaveSummaryData, DataError>{
        return leaveRemoteRepository.fetchLeaveSummary().map {
            it.toData()
        }
    }
}