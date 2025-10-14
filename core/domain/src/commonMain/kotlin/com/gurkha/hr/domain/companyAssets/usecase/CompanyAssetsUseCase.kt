package com.gurkha.hr.domain.companyAssets.usecase

import com.gurkha.hr.domain.companyAssets.mapper.toData
import com.gurkha.hr.domain.companyAssets.model.CompanyAssetsData
import com.gurkha.hr.domain.companyAssets.repository.CompanyAssetsRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class CompanyAssetsUseCase(
    private val companyAssetsRemoteRepository: CompanyAssetsRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<CompanyAssetsData>, DataError>{
        return companyAssetsRemoteRepository.getCompanyAssets().map {
            it.toData()
        }
    }
}