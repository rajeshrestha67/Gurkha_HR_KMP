package com.gurkha.hr.domain.upComingEvent.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.upComingEvents.EventsResponseDto

interface EventRemoteRepository {
    suspend fun fetchEvents(): ERPResult<EventsResponseDto, DataError>
}