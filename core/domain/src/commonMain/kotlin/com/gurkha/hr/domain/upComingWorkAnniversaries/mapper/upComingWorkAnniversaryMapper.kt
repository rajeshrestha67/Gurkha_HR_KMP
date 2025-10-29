package com.gurkha.hr.domain.upComingWorkAnniversaries.mapper

import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData
import com.gurkha.model.upComingBirthday.ui.ViewAllUi
import com.gurkha.model.upComingWorkAnniversaries.UpComingWorkAnniversaryDetailDto

fun UpComingWorkAnniversaryDetailDto.toData(): List<UpComingWorkAnniversaryData>{
    return detail?.map {
        UpComingWorkAnniversaryData(
            fullName = it.fullName ?: "",
            designationName = it.designationName ?: "",
            imageUrl = it.imageUrl ?: "",
            branchName = it.branchName ?: "",
            joinedDate =  it.joinedDate?.split("T")[0] ?: "" ,
            initials = it.fullName?.extractInitials() ?: ""
        )
    } ?: emptyList()
}


fun List<UpComingWorkAnniversaryData>.toUi(): List<ViewAllUi> {
    return map {
        ViewAllUi(
            fullName = it.fullName,
            designationName = it.designationName,
            branchName = it.branchName,
            imageUrl = it.imageUrl,
            initials = it.initials,
            date = it.joinedDate
        )
    }
}