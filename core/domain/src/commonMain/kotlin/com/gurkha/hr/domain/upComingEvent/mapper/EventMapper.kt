package com.gurkha.hr.domain.upComingEvent.mapper

import com.gurkha.hr.domain.upComingEvent.model.EventData
import com.gurkha.model.upComingBirthday.ui.ViewAllUi
import com.gurkha.model.upComingEvents.EventsResponseDto

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

fun List<EventData>.toUi(): List<ViewAllUi>{
    return map {
        ViewAllUi(
            title = it.name,
            description = it.description,
            toDate = it.toDateBs,
            fromDate = it.fromDateBs,
        )
    }
}