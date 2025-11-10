package com.gurkha.hr.domain.upComingBirthday.mapper

import androidx.compose.ui.graphics.Color
import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.model.upComingBirthday.UserUpComingBirthdayDetailDto
import com.gurkha.model.upComingBirthday.ui.ViewAllUi
import kotlin.random.Random


fun UserUpComingBirthdayDetailDto.toData(): List<UpComingBirthdayData> {
    return detail?.map {
        UpComingBirthdayData(
            fullName = it.fullName ?: "",
            dateOfBirth = it.dateOfBirth ?: "",
            designationName = it.designationName ?: "",
            branchName = it.branchName ?: "",
            initials = it.fullName?.extractInitials() ?: "",
            employeeId = it.id?.toLong() ?: 0L,
            chatId = it.chatId ?: "",
            employeeName = it.fullName ?: "",
            imageUrl = it.imageUrl,
            phoneNumber = it.phoneNumber ?: "",
            backgroundColor = randomLightColor()
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
private fun randomLightColor(): Color {
    val rnd = Random.Default
    // Ensure values are closer to 255 (light colors)
    val r = 150 + rnd.nextInt(106) // 150–255
    val g = 150 + rnd.nextInt(106)
    val b = 150 + rnd.nextInt(106)
    return Color(r, g, b)
}