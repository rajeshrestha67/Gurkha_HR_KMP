package com.gurkha.hr.domain.upComingEvent.mapper

import com.gurkha.hr.domain.upComingEvent.model.EventData
import com.gurkha.upComingEvents.EventsResponseDto

fun EventsResponseDto.toData(): List<EventData> {
    return detail?.map {
        EventData(
            name = it.name ?: "",
            description = it.description ?: "",
            toDateBs = it.toDateBs ?: "",
            fromDateBs = it.fromDateBs ?: "",
            isNotice = it.isNotice ?: ""
        )
    } ?: emptyList()
}