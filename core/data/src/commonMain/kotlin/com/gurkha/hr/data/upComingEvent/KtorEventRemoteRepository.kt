package com.gurkha.hr.data.upComingEvent

import com.gurkha.hr.domain.upComingEvent.repository.EventRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.upComingEvents.EventsResponseDto
import io.ktor.client.HttpClient

class KtorEventRemoteRepository(
    private val httpClient: HttpClient
): EventRemoteRepository {
    override suspend fun fetchEvents(): ERPResult<EventsResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.UPCOMING_EVENT_END_POINT
            )
        }
    }
}