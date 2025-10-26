package com.gurkha.hr.domain.history.useCase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.history.mapper.toData
import com.gurkha.hr.domain.history.model.HistoryData
import com.gurkha.hr.domain.history.repository.HistoryRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class HistoryUseCase(
    private val historyRemoteRepository: HistoryRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(
        bsMonth: Int,
        bsYear: Int
    ): ERPResult<List<HistoryData>, DataError> {
        val employeeId = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
        return historyRemoteRepository.getHistoryLeave(
            bsMonth = bsMonth,
            bsYear = bsYear,
            branchId = null,
            employeeId = employeeId,
            isSelf = "Y"
        ).map { it.toData()
        }
    }
}