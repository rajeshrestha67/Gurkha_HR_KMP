package com.gurkha.hr.domain.reportScreen.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.reportScreen.ReportSummaryResponseDTO

interface ReportRemoteRepository {
    suspend fun getReportSummary(
        bsMonth: Int?,
        bsYear: Int?,
        branchId: String?,
        employeeId: Int?,
        isSelf: String?
    ): ERPResult<ReportSummaryResponseDTO, DataError>
}