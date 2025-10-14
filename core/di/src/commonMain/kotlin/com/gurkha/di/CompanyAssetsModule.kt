package com.gurkha.di

import com.gurkha.hr.data.companyAssets.KtorCompanyAssetsRemoteRepository
import com.gurkha.hr.domain.companyAssets.repository.CompanyAssetsRemoteRepository
import com.gurkha.hr.domain.companyAssets.usecase.CompanyAssetsUseCase
import com.gurkha.hr.profile.company_assets.CompanyAssetsViewModel
import io.ktor.client.HttpClient
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class CompanyAssetsModule {
    @Factory(binds = [CompanyAssetsRemoteRepository::class])
    fun companyAssetsRemoteRepository(httpClient: HttpClient): CompanyAssetsRemoteRepository =
        KtorCompanyAssetsRemoteRepository(httpClient)

    @Factory
    fun companyAssetsUseCase(companyAssetsRemoteRepository: CompanyAssetsRemoteRepository): CompanyAssetsUseCase =
        CompanyAssetsUseCase(companyAssetsRemoteRepository=companyAssetsRemoteRepository)

    @Factory
    fun getCompanyAssetsScreenViewModel(
        companyAssetsUseCase: CompanyAssetsUseCase
    ): CompanyAssetsViewModel = CompanyAssetsViewModel(
        companyAssetsUseCase = companyAssetsUseCase
    )
}