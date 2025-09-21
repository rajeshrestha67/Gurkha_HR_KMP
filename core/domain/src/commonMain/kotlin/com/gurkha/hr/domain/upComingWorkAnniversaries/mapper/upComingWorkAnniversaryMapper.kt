package com.gurkha.hr.domain.upComingWorkAnniversaries.mapper

import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData
import com.gurkha.model.upComingWorkAnniversaries.UpComingWorkAnniversaryDetailDto

fun UpComingWorkAnniversaryDetailDto.toData(): List<UpComingWorkAnniversaryData>{
    return detail?.map {
        UpComingWorkAnniversaryData(
            fullName = it.fullName ?: "",
            designationName = it.designationName ?: "",
            imageUrl = it.imageUrl ?: "",
            branchName = it.branchName ?: "",
            joinedDate = it.joinedDate ?: ""
        )
    } ?: emptyList()
}