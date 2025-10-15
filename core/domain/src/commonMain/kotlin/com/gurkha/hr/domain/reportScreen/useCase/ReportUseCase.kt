package com.gurkha.hr.domain.reportScreen.useCase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.reportScreen.mapper.toData
import com.gurkha.hr.domain.reportScreen.model.ReportData
import com.gurkha.hr.domain.reportScreen.repository.ReportRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class ReportUseCase(
    private val reportRemoteRepository: ReportRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
   suspend operator fun invoke(
        bsMonth: Int,
        bsYear: Int
    ): ERPResult<List<ReportData>, DataError> {
       val employeeId = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
       return reportRemoteRepository.getReportSummary(
           bsMonth = bsMonth,
           bsYear = bsYear,
           branchId = null,
           employeeId = employeeId,
           isSelf = "Y"
       ).map { it.toData() }
   }
}