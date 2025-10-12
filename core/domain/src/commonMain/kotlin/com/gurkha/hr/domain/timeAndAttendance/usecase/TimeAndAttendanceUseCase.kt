package com.gurkha.hr.domain.timeAndAttendance.usecase

import com.gurkha.hr.domain.timeAndAttendance.mapper.toData
import com.gurkha.hr.domain.timeAndAttendance.model.TimeAndAttendanceData
import com.gurkha.hr.domain.timeAndAttendance.repository.TimeAndAttendanceRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class TimeAndAttendanceUseCase(
    private val timeAndAttendanceRemoteRepository: TimeAndAttendanceRemoteRepository,
) {
    suspend operator fun invoke(
        fromDate: String,
        toDate: String,
    ): ERPResult<List<TimeAndAttendanceData>, DataError> {
        return timeAndAttendanceRemoteRepository.fetchTimeAndAttendance(
            dateFrom = fromDate,
            toDate = toDate
        ).map {
            it.toData()
        }
    }
}