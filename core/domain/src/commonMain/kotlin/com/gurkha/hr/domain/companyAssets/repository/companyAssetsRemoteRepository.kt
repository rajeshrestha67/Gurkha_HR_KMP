package com.gurkha.hr.domain.companyAssets.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.companyAssets.CompanyAssetResponseDto
import com.gurkha.model.network.DataError

interface CompanyAssetsRemoteRepository{
suspend fun getCompanyAssets(
    id: Int
): ERPResult<CompanyAssetResponseDto, DataError>

}