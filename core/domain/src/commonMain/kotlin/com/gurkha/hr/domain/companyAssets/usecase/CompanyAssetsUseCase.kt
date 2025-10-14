package com.gurkha.hr.domain.companyAssets.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.companyAssets.mapper.toData
import com.gurkha.hr.domain.companyAssets.model.CompanyAssetsData
import com.gurkha.hr.domain.companyAssets.repository.CompanyAssetsRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class CompanyAssetsUseCase(
    private val companyAssetsRemoteRepository: CompanyAssetsRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(): ERPResult<List<CompanyAssetsData>, DataError>{
        val id = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
        return companyAssetsRemoteRepository.getCompanyAssets(
            id
        ).map {
            it.toData()
        }
    }
}