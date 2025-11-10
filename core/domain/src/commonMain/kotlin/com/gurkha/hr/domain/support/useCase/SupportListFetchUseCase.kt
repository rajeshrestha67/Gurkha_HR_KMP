package com.gurkha.hr.domain.support.useCase

import com.gurkha.hr.domain.support.mapper.toData
import com.gurkha.hr.domain.support.model.SupportListData
import com.gurkha.hr.domain.support.repository.SupportListRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class SupportListFetchUseCase(
    private val supportListRemoteRepository: SupportListRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<SupportListData>, DataError>{
        return supportListRemoteRepository.getSupportList().map {
            it.toData()
        }
    }
}