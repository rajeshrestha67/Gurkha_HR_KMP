package com.gurkha.hr.data.support

import com.gurkha.hr.domain.support.repository.SupportListRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.support.SupportResponseDto
import io.ktor.client.HttpClient

class KtorSupportListRepository(
    private val httpClient: HttpClient
): SupportListRemoteRepository {
    override suspend fun getSupportList(): ERPResult<SupportResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.SUPPORT_LIST_END_POINT
            )
        }
    }
}