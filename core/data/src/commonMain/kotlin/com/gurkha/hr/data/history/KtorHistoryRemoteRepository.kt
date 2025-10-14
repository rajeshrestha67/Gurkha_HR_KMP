package com.gurkha.hr.data.history

import com.gurkha.hr.domain.history.repository.HistoryRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.history.HistoryRequestDTO
import com.gurkha.model.history.HistoryResponseDTO
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorHistoryRemoteRepository(private val httpClient: HttpClient):
    HistoryRemoteRepository{
    override suspend fun getHistoryLeave(
        bsMonth: Int?,
        bsYear: Int?,
        branchId: String?,
        employeeId: Int?,
        isSelf: String?
    ): ERPResult<HistoryResponseDTO, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.HISTORY_END_POINT
            ){
                setBody(HistoryRequestDTO(
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