package com.gurkha.hr.profile.model.companyAssets

import com.gurkha.hr.domain.companyAssets.model.CompanyAssetsData

data class CompanyAssetsState(
    val isLoading: Boolean = false,
    val companyAssetsList: List<CompanyAssetsData> = emptyList(),
)