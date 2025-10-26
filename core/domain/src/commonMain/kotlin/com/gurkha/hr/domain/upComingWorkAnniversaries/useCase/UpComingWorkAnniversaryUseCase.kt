package com.gurkha.hr.domain.upComingWorkAnniversaries.useCase

import com.gurkha.hr.domain.upComingWorkAnniversaries.mapper.toData
import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData
import com.gurkha.hr.domain.upComingWorkAnniversaries.repository.UpComingWorkAnniversaryRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class UpComingWorkAnniversaryUseCase(
    private val upComingWorkAnniversaryRemoteRepository: UpComingWorkAnniversaryRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<UpComingWorkAnniversaryData>, DataError> {
        return upComingWorkAnniversaryRemoteRepository.fetchUpComingWorkAnniversary().map {
            it.toData()
        }
    }
}