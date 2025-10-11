package com.gurkha.hr.domain.allocatedLeave.usecase

import com.gurkha.hr.domain.allocatedLeave.mapper.toData
import com.gurkha.hr.domain.allocatedLeave.model.AllocatedLeaveData
import com.gurkha.hr.domain.allocatedLeave.repository.AllocatedLeaveRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError


class AllocatedLeaveUseCase(
    private val allocatedLeaveRemoteRepository: AllocatedLeaveRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<AllocatedLeaveData>, DataError> {
        return allocatedLeaveRemoteRepository.getAllocatedLeave().map {
            it.toData()
        }
    }
}
