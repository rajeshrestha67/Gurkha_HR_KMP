package com.gurkha.hr.domain.upComingBirthday.mapper

import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.model.upComingBirthday.UserUpComingBirthdayDetailDto


fun UserUpComingBirthdayDetailDto.toData(): List<UpComingBirthdayData> {
    return detail?.map {
        UpComingBirthdayData(
            fullName = it.fullName ?: "",
            dateOfBirth = it.dateOfBirth ?: "",
            designationName = it.designationName ?: "",
            branchName = it.branchName ?: "",
            imageUrl = it.imageUrl ?: ""
        )
    } ?: emptyList()
}