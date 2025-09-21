package com.gurkha.hr.domain.userDetail.mapper

import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.model.userDetail.UserDetailResponseDto

fun UserDetailResponseDto.toData(): UserDetailData {
    println("data $detail")
    return UserDetailData(
        email = detail?.email ?: "",
        phone = detail?.phone ?: "",
//        userProfileUrl = detail?.imageUrl ?: "",
        userProfileUrl = detail?.imageUrl ?: "",
        fullName = detail?.fullName ?: "",
        levelName = detail?.levelName ?: "",
    )
}
