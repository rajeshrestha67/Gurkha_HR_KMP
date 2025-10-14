package com.gurkha.hr.domain.companyAssets.mapper

import com.gurkha.hr.domain.companyAssets.model.CompanyAssetsData
import com.gurkha.model.companyAssets.CompanyAssetResponseDto

fun CompanyAssetResponseDto.toData(): List<CompanyAssetsData>{
    return detail.assetDetails?.map {
        CompanyAssetsData(
            assetsId = it.id?: 0,
            assetsName = it.assetName?: "",
            assetsDescription = it.assetDescription?:"",
            dateInBs = it.dateInBs?: "" ,
            active = it.active?:""
        )
    } ?:emptyList() }