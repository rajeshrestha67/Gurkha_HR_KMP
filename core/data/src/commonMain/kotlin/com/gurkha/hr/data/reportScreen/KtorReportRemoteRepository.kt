package com.gurkha.hr.data.reportScreen

import com.gurkha.hr.domain.history.repository.HistoryRemoteRepository
import com.gurkha.hr.domain.reportScreen.repository.ReportRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.history.HistoryResponseDTO
import com.gurkha.model.network.DataError
import com.gurkha.model.reportScreen.ReportRequestDTO
import com.gurkha.model.reportScreen.ReportSummaryResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorReportRemoteRepository(private val httpClient: HttpClient) :
    ReportRemoteRepository {
    override suspend fun getReportSummary(
        bsMonth: Int?,
        bsYear: Int?,
        branchId: String?,
        employeeId: Int?,
        isSelf: String?
    ): ERPResult<ReportSummaryResponseDTO, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.REPORT_END_POINT
            ){
                setBody(ReportRequestDTO(
                    bsMonth = bsMonth,
                    bsYear = bsYear,
                    branchId = branchId,
                    employeeId = employeeId,
                    isSelf = isSelf
                ))
            }
        }
    }

}