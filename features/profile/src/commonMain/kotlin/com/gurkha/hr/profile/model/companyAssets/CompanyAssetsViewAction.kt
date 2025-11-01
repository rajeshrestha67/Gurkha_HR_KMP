package com.gurkha.hr.profile.model.companyAssets

interface CompanyAssetsViewAction {
    data object OnFetchData : CompanyAssetsViewAction

    data object OnRefresh: CompanyAssetsViewAction
}