package com.gurkha.hr.domain.allocatedLeave.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.allocatedLeave.mapper.toData
import com.gurkha.hr.domain.allocatedLeave.model.AllocatedLeaveData
import com.gurkha.hr.domain.allocatedLeave.repository.AllocatedLeaveRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull


class AllocatedLeaveUseCase(
    private val allocatedLeaveRemoteRepository: AllocatedLeaveRemoteRepository,
    private val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke(): ERPResult<List<AllocatedLeaveData>, DataError> {
        val id = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
        return allocatedLeaveRemoteRepository.getAllocatedLeave(id).map {
            it.toData()
        }
    }
}
