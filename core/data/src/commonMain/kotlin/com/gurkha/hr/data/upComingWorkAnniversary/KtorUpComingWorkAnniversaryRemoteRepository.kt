package com.gurkha.hr.data.upComingWorkAnniversary

import com.gurkha.hr.domain.upComingWorkAnniversaries.repository.UpComingWorkAnniversaryRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.upComingWorkAnniversaries.UpComingWorkAnniversaryDetailDto
import io.ktor.client.HttpClient


class KtorUpComingWorkAnniversaryRemoteRepository(
    private val httpClient: HttpClient
) : UpComingWorkAnniversaryRemoteRepository {
    override suspend fun fetchUpComingWorkAnniversary(): ERPResult<UpComingWorkAnniversaryDetailDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.UPCOMING_WORK_ANNIVERSARY_END_POINT
            )
        }
    }
}