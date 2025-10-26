package com.gurkha.hr.domain.upComingEvent.useCase

import com.gurkha.hr.domain.upComingEvent.mapper.toData
import com.gurkha.hr.domain.upComingEvent.model.EventData
import com.gurkha.hr.domain.upComingEvent.repository.EventRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class EventUseCase(
    private val eventRemoteRepository: EventRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<EventData>, DataError>{
        return eventRemoteRepository.fetchEvents().map {
            it.toData()
        }
    }
}