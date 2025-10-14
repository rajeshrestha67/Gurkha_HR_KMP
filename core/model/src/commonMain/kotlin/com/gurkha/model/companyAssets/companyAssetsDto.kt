package com.gurkha.model.companyAssets

import kotlinx.serialization.Serializable

@Serializable
data class CompanyAssetResponseDto(
    val status: String,
    val message: String,
    val detail: Detail,
    val success: Boolean
)
@Serializable
data class Detail(
    val id: String?,
    val employeeName: String,
    val employeeId: Int,
    val assetDetails: List<AssetDetail>
)
@Serializable
data class AssetDetail(
    val id: Int,
    val assetName: String,
    val assetDescription: String,
    val date: String,
    val dateInBs: String,
    val active: String
)
