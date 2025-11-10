package com.gurkha.hr.domain.upComingBirthday.mapper

import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.model.upComingBirthday.UserUpComingBirthdayDetailDto
import com.gurkha.model.upComingBirthday.ui.ViewAllUi


fun UserUpComingBirthdayDetailDto.toData(): List<UpComingBirthdayData> {
    return detail?.map {
        UpComingBirthdayData(
            fullName = it.fullName ?: "",
            dateOfBirth = it.dateOfBirth ?: "",
            designationName = it.designationName ?: "",
            branchName = it.branchName ?: "",
            imageUrl = it.imageUrl ?: "",
            initials = it.fullName?.extractInitials() ?: "",
            employeeId = it.id?.toLong() ?: 0L,
            chatId = it.chatId ?: "",
            employeeName = it.fullName ?: "",
            profileImageUrl = it.imageUrl ?: "",
            phoneNumber = it.phoneNumber ?: ""
        )
    } ?: emptyList()
}



fun List<UpComingBirthdayData>.toUi(): List<ViewAllUi> {
    return map {
        ViewAllUi(
            fullName = it.fullName,
            designationName = it.designationName,
            branchName = it.branchName,
            imageUrl = it.imageUrl,
            initials = it.initials,
            date = it.dateOfBirth
        )
    }
}
