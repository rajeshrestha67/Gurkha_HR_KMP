package com.gurkha.hr.domain.upComingBirthday.usecase

import com.gurkha.hr.domain.upComingBirthday.mapper.toData
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.domain.upComingBirthday.repository.UpComingBirthdayRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class UpComingBirthdayUseCase(
    private val upComingBirthdayRemoteRepository: UpComingBirthdayRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<UpComingBirthdayData>, DataError> {
        return upComingBirthdayRemoteRepository.fetchUpComingBirthday().map {
            it.toData()
        }
    }
}