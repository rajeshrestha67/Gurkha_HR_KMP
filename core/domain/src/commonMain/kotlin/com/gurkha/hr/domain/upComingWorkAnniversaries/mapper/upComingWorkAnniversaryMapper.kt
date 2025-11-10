package com.gurkha.hr.domain.upComingWorkAnniversaries.mapper

import androidx.compose.ui.graphics.Color
import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData
import com.gurkha.model.upComingBirthday.ui.ViewAllUi
import com.gurkha.model.upComingWorkAnniversaries.UpComingWorkAnniversaryDetailDto
import kotlin.random.Random

fun UpComingWorkAnniversaryDetailDto.toData(): List<UpComingWorkAnniversaryData>{
    return detail?.map {
        UpComingWorkAnniversaryData(
            fullName = it.fullName ?: "",
            designationName = it.designationName ?: "",
            imageUrl = it.imageUrl ?: "",
            branchName = it.branchName ?: "",
            joinedDate =  it.joinedDate?.split(" ")?.getOrNull(0)?: "" ,
            initials = it.fullName?.extractInitials() ?: "",
            backgroundColor = randomLightColor()
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
            date = it.joinedDate,
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