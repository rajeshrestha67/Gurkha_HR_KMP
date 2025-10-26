package com.gurkha.hr.domain.history.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.history.HistoryResponseDTO
import com.gurkha.model.network.DataError

interface HistoryRemoteRepository {
    suspend fun getHistoryLeave(
        bsMonth: Int?,
        bsYear: Int?,
        branchId: String?,
        employeeId: Int?,
        isSelf: String?
    ): ERPResult<HistoryResponseDTO, DataError>
}