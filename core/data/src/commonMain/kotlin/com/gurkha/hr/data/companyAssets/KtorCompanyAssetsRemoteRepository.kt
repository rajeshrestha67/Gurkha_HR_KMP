package com.gurkha.hr.data.companyAssets

import com.gurkha.hr.domain.companyAssets.repository.CompanyAssetsRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.companyAssets.CompanyAssetResponseDto
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient

class KtorCompanyAssetsRemoteRepository(
    private val httpClient: HttpClient):
    CompanyAssetsRemoteRepository {
    override suspend fun getCompanyAssets(): ERPResult<CompanyAssetResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.COMPANY_ASSETS_ENDPOINT
            )
        }
    }

}